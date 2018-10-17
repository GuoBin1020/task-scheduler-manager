package com.example.tsm.job;

import com.example.tsm.cache.SenderCache;
import com.example.tsm.entity.SenderEntity;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
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
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 获得发件人ID
        String senderId = (String) jobExecutionContext.getJobDetail().getJobDataMap().get("senderId");
        SenderEntity senderEntity = SenderCache.get(senderId);
        // 获得接收人
        String receiver = (String) jobExecutionContext.getJobDetail().getJobDataMap().get("receivers");
        // 获得主题
        String subject = (String) jobExecutionContext.getJobDetail().getJobDataMap().get("subject");
        // 获得内容
        String context = (String) jobExecutionContext.getJobDetail().getJobDataMap().get("context");

        JavaMailSender javaMailSender = getJavaMailSender(senderId);
        if (javaMailSender == null) {
            log.error("JavaMailSender objct get failed.");
            return;
        }
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            // 发送者
            helper.setFrom(senderEntity.getUsername());
            // 接收者，可多个用英文分号分隔
            String receivers[] = receiver.split(";");
            helper.setTo(receivers);
            // 主题
            helper.setSubject(subject);
            // 内容
            helper.setText(context);
            // 添加附件
            String filepath = "C:\\Users\\Administrator\\Desktoptask-scheduler-manager.zip";
            FileSystemResource file = new FileSystemResource(new File(filepath));
            String fileName = filepath.substring(filepath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);   // 可以写多条这个语句添加多个
            javaMailSender.send(message);
            log.info("Send email to {} Success!", "我自己的");
        } catch (MessagingException e) {
            log.error("MimeMessageHelper实例化失败！！！", e);
        }

//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        simpleMailMessage.setFrom(senderEntity.getUsername());
//        String receivers[] = receiver.split(";");
//        simpleMailMessage.setTo(receivers);
//        simpleMailMessage.setSubject(subject);
//        simpleMailMessage.setText(context);

//        javaMailSender.send(message);
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
