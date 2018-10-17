package com.example.tsm.service;

import com.example.tsm.job.SenderEmailTimer;
import com.example.tsm.model.JobRequest;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class EmailService {

    /**
     * 注入任务调度器
     */
    private final Scheduler scheduler;

    @Autowired
    public EmailService(@Qualifier("VaeScheduler") Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * 添加定时任务
     * @param request
     * @throws Exception
     */
    public void addJob(JobRequest request) throws Exception {
        // 检查任务
        validata(request.getJobName(), request.getJobGroup());
        // 创建表达式
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(request.getCronExpression());
        // 创建任务
        JobDetail jobDetail = JobBuilder
                .newJob(SenderEmailTimer.class)
                .withIdentity(request.getJobName(), request.getJobGroup())
                .build();
        // 置入参数
        // 发送人ID
        jobDetail.getJobDataMap().put("senderId", request.getSenderId());
        // 接收人邮箱
        jobDetail.getJobDataMap().put("receivers", request.getReceivers());
        // 邮箱主题
        jobDetail.getJobDataMap().put("subject", request.getSubject());
        // 邮箱内容
        jobDetail.getJobDataMap().put("context", request.getContext());
        // 创建任务触发器
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(request.getJobName(), request.getJobGroup())
                .withSchedule(scheduleBuilder)
                .build();
        // 将触发器与任务绑定到调度器内
        scheduler.scheduleJob(jobDetail, trigger);
        log.info("任务:{}.{}添加完成！！！", request.getJobGroup(), request.getJobName());
    }

    // 检查任务
    private void validata(String jobName, String jobGroup) {
        try {
            if (scheduler.checkExists(JobKey.jobKey(jobName, jobGroup))) {
                scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
            }
        } catch (SchedulerException e) {
            log.error("检查过程中发生错误！！！", e);
        }
    }

}
