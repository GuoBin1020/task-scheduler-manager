package com.example.tsm.service;

import com.example.tsm.dao.SenderRepository;
import com.example.tsm.dao.TimedTaskRepository;
import com.example.tsm.entity.TimedTaskEntity;
import com.example.tsm.job.SenderEmailTimer;
import com.example.tsm.model.JobRequest;
import com.example.tsm.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class EmailService {

    /**
     * 注入任务调度器
     */
    private final Scheduler scheduler;
    /**
     * 任务数据接口
     */
    private final TimedTaskRepository timedTaskRepository;
    /**
     * 发送人数据接口
     */
    private final SenderRepository senderRepository;

    @Autowired
    public EmailService(@Qualifier("Scheduler") Scheduler scheduler, TimedTaskRepository timedTaskRepository, SenderRepository senderRepository) {
        this.scheduler = scheduler;
        this.timedTaskRepository = timedTaskRepository;
        this.senderRepository = senderRepository;
    }

    private static final String TASK_STATUS_RUNNING = "Running";
    private static final String TASK_STATUS_PENDING = "Pending";

    public void checkTaskExecuteCount(String taskId) throws Exception {
        TimedTaskEntity timedTaskEntity = timedTaskRepository.findById(taskId).orElse(null);
        if (timedTaskEntity != null && timedTaskEntity.getExecuteCount() == 0) {
            // 停止任务
            pauseJob(taskId);
        }
    }

    /**
     * 添加或更新任务
     *
     * @param request 请求数据
     * @throws Exception 异常
     */
    public void addOrUpdateJob(JobRequest request) throws Exception {
        // 认证发送人
        if (!senderRepository.existsById(request.getSenderId())) {
            log.error("发送人不存在！！！");
            throw new Exception("发送人不存在！！！");
        }

        // 创建任务
        JobDetail jobDetail = createJobDetail(request);
        // 创建任务触发器
        Trigger trigger = createTrigger(request);

        // 将触发器与任务绑定到调度器内
        scheduler.scheduleJob(jobDetail, trigger);

        // 数据入库
        timedTaskRepository.saveAndFlush(new TimedTaskEntity(request));
        log.info("任务:{}.{}添加完成！！！", request.getJobGroup(), request.getJobName());
    }

    /**
     * 删除任务
     * @param taskId 任务ID
     * @throws Exception 异常
     */
    public void deleteJob(String taskId) throws Exception {
        // 获得Task
        TimedTaskEntity timedTaskEntity = timedTaskRepository.findById(taskId).orElse(null);
        if (timedTaskEntity != null) {
            String jobName = timedTaskEntity.getJobName();
            String jobGroup = timedTaskEntity.getJobGroup();
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
            timedTaskRepository.deleteById(taskId);
            log.info("删除任务{}.{}完成！！！", jobName, jobGroup);
        }
    }

    /**
     * 暂停任务
     * @param taskId 任务ID
     * @throws Exception 异常
     */
    public void pauseJob(String taskId) throws Exception {
        // 获得Task
        TimedTaskEntity timedTaskEntity = timedTaskRepository.findById(taskId).orElse(null);
        if (timedTaskEntity != null) {
            String jobName = timedTaskEntity.getJobName();
            String jobGroup = timedTaskEntity.getJobGroup();
            scheduler.pauseJob(JobKey.jobKey(jobName, jobGroup));
            // 更新状态
            timedTaskEntity.setStatus(TASK_STATUS_PENDING);
            timedTaskRepository.saveAndFlush(timedTaskEntity);
            log.info("暂停任务{}.{}完成！！！", jobName, jobGroup);
        }
    }

    /**
     * 恢复任务
     * @param taskId 任务ID
     * @throws Exception 异常
     */
    public void resumeJob(String taskId) throws Exception {
        // 获得Task
        TimedTaskEntity timedTaskEntity = timedTaskRepository.findById(taskId).orElse(null);
        if (timedTaskEntity != null && timedTaskEntity.getExecuteCount() > 0) {
            String jobName = timedTaskEntity.getJobName();
            String jobGroup = timedTaskEntity.getJobGroup();
            scheduler.resumeJob(JobKey.jobKey(jobName, jobGroup));
            // 更新状态
            timedTaskEntity.setStatus(TASK_STATUS_RUNNING);
            timedTaskRepository.saveAndFlush(timedTaskEntity);
            log.info("恢复任务{}.{}完成！！！", jobName, jobGroup);
        } else {
            log.info("任务执行次数为0，无法恢复。");
        }
    }



    // 创建任务触发器
    private Trigger createTrigger(JobRequest request) throws Exception {
        // 创建表达式
        CronScheduleBuilder scheduleBuilder = createCronSchedule(request);
        return TriggerBuilder
                .newTrigger()
                .withIdentity(request.getJobName(), request.getJobGroup())
                .withSchedule(scheduleBuilder)
                // 延迟启动
                .startAt(new Date(System.currentTimeMillis() + (request.getDelayTime() * 1000)))
                .build();
    }

    // 创建表达式
    private CronScheduleBuilder createCronSchedule(JobRequest request) throws Exception {
        String cronExpression;
        if (request.getCronExpression() != null && !request.getCronExpression().trim().isEmpty()) {
            cronExpression = request.getCronExpression();
        } else {
            if (request.getIntervalTime() != null && request.getIntervalTime() >= 1) {
                cronExpression = generateCron(request.getIntervalTime());
            } else {
                log.error("间隔时间最少为1秒！！！");
                throw new Exception("间隔时间最少为1秒！！！");
            }
        }
        // 增加这个withMisfireHandlingInstructionDoNothing能防止任务恢复，即大量补充执行
        return CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();
    }

    // 生成表达式
    private String generateCron(Long intervalTime) {
        if (intervalTime >= 365 * 24 * 60 * 60) {
            return String.format("0 0 0 1 1 ? */%s", intervalTime / (365 * 24 * 60 * 60));
        } else if (intervalTime >= 30 * 24 * 60 * 60) {
            return String.format("0 0 0 1 */%s ?", intervalTime / (30 * 24 * 60 * 60));
        } else if (intervalTime >= 24 * 60 * 60) {
            return String.format("0 0 0 */%s * ?", intervalTime / (24 * 60 * 60));
        } else if (intervalTime >= 60 * 60) {
            return String.format("0 0 */%s * * ?", intervalTime / (60 * 60));
        } else if (intervalTime >= 60) {
            return String.format("0 */%s * * * ?", intervalTime / 60);
        } else {
            return String.format("*/%s * * * * ?", intervalTime);
        }
    }

    // 创建任务
    private JobDetail createJobDetail(JobRequest request) throws SchedulerException {
        // 检查任务是否存在,存在创建任务删除任务
        if (validata(request.getJobName(), request.getJobGroup())) {
            scheduler.deleteJob(JobKey.jobKey(request.getJobName(), request.getJobGroup()));
        }
        JobDetail jobDetail = JobBuilder
                .newJob(SenderEmailTimer.class)
                .withIdentity(request.getJobName(), request.getJobGroup())
                .build();
        // 置入参数
        jobDetail.getJobDataMap().put("request", JSONUtil.toJson(request));
        return jobDetail;
    }

    // 检查任务
    private boolean validata(String jobName, String jobGroup) throws SchedulerException {
        try {
            return scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
        } catch (SchedulerException e) {
            log.error("检查过程中发生错误！！！", e);
            throw e;
        }
    }

}
