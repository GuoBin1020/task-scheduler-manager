package com.example.tsm.job;

import com.example.tsm.cache.SenderCache;
import com.example.tsm.dao.TimedTaskRepository;
import com.example.tsm.entity.SenderEntity;
import com.example.tsm.entity.TimedTaskEntity;
import com.example.tsm.model.JobRequest;
import com.example.tsm.service.EmailService;
import com.example.tsm.utils.JSONUtil;
import com.example.tsm.utils.SpringIOCUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SenderEmailTimer extends QuartzJobBean {

    private static final ConcurrentHashMap<String, JavaMailSenderImpl> senderMap = new ConcurrentHashMap<>();

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        String jobRequestJsonStr = jobExecutionContext.getJobDetail().getJobDataMap().get("request").toString();
        JobRequest request = JSONUtil.fromJson(jobRequestJsonStr, JobRequest.class);
        if (request == null) {
            log.error("获得JobRequest对象失败！！！");
            return;
        }
        SenderEntity senderEntity = SenderCache.get(request.getSenderId());
        JavaMailSender javaMailSender = getJavaMailSender(request.getSenderId());
        if (javaMailSender == null) {
            log.error("获得JavaMailSender对象失败！！！.");
            return;
        }
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            // 发送者
            helper.setFrom(senderEntity.getUsername());
            // 接收者，可多个用英文分号分隔
            String receivers[] = request.getReceivers().split(";");
            helper.setTo(receivers);
            // 主题
            helper.setSubject(request.getSubject());
            // 内容
            helper.setText(request.getContext());
            // 添加附件
            if (request.getAttachmentPath() != null && !request.getAttachmentPath().trim().isEmpty()) {
                FileSystemResource file = new FileSystemResource(new File(request.getAttachmentPath()));
                String fileName = request.getAttachmentPath().substring(request.getAttachmentPath().lastIndexOf(File.separator));
                helper.addAttachment(fileName, file);   // 可以写多条这个语句添加多个
            }
            // javaMailSender.send(message);
            log.info("发送邮件给{}成功!", request.getReceivers());
        } catch (MessagingException e) {
            log.error("MimeMessageHelper实例化失败！！！", e);
        }
        taskCount(request.getTaskId());
    }

    // 任务计数
    private void taskCount(String taskId) {
        // 计数
        TimedTaskRepository timedTaskRepository = (TimedTaskRepository) SpringIOCUtil.getBeanByName("timedTaskRepository");
        if (timedTaskRepository != null) {
            // 获得任务信息
            TimedTaskEntity timedTaskEntity = timedTaskRepository.findById(taskId).orElse(null);
            if (timedTaskEntity != null && timedTaskEntity.getExecuteCount() > 0) {
                // 开始计数
                timedTaskEntity.setExecuteCount(timedTaskEntity.getExecuteCount() - 1);
                timedTaskRepository.saveAndFlush(timedTaskEntity);
            }
        }

        // 最后处理
        try {
            EmailService emailService = (EmailService) SpringIOCUtil.getBeanByName("emailService");
            if (emailService != null) {
                emailService.checkTaskExecuteCount(taskId);
            }
        } catch (Exception e) {
            log.error("处理任务计数失败！！！", e);
        }
    }

    private synchronized JavaMailSender getJavaMailSender(String senderId) {
        JavaMailSenderImpl javaMailSender = senderMap.get(senderId);
        if (javaMailSender == null) {
            SenderEntity senderEntity = SenderCache.get(senderId);
            javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setHost(senderEntity.getHost());
            javaMailSender.setProtocol(senderEntity.getProtocol());
            javaMailSender.setUsername(senderEntity.getUsername());
            javaMailSender.setPassword(senderEntity.getPassword());
            senderMap.put(senderEntity.getSenderId(), javaMailSender);
        }
        return javaMailSender;
    }
}
