package com.example.tsm.wrapper;

import com.example.tsm.model.JobInfo;
import com.example.tsm.utils.JobUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class JobWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobWrapper.class);

    private final Scheduler scheduler;

    @Autowired
    public JobWrapper(@Qualifier("Scheduler") Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void addJob(JobInfo jobInfo) throws Exception {
        // 构建Job信息
        JobDetail jobDetail;
        try {
            jobDetail = JobBuilder
                    .newJob(JobUtil.getJob(jobInfo.getJobClassName()).getClass())
                    .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroupName())
                    .build();
        } catch (ClassNotFoundException e) {
            LOGGER.error("找不到要加载的类!!!", e);
            throw e;
        } catch (IllegalAccessException e) {
            LOGGER.error("参数异常!!!", e);
            throw e;
        } catch (InstantiationException e) {
            LOGGER.error("类的实例化失败!!!", e);
            throw e;
        }

        // 设置调度器的表达式
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobInfo.getCronExpression());

        // 构建新的触发器
        CronTrigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroupName())
                .withSchedule(scheduleBuilder)
                .build();

        // 执行定时任务
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            LOGGER.error("执行定时任务失败!!!", e);
            throw e;
        }
    }

    public void pauseJob(JobInfo jobInfo) throws Exception {
        try {
            scheduler.pauseJob(JobKey.jobKey(jobInfo.getJobName(), jobInfo.getJobGroupName()));
        } catch (SchedulerException e) {
            LOGGER.error("暂停任务失败!!!", e);
            throw e;
        }
    }

    public void resumeJob(JobInfo jobInfo) throws Exception {
        try {
            scheduler.resumeJob(JobKey.jobKey(jobInfo.getJobName(), jobInfo.getJobGroupName()));
        } catch (SchedulerException e) {
            LOGGER.error("恢复任务失败!!!", e);
            throw e;
        }
    }


}
