package com.example.tsm.entity;

import com.example.tsm.model.JobRequest;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "timed_task")
@Data
public class TimedTaskEntity {

    public TimedTaskEntity() {}

    public TimedTaskEntity(JobRequest jobRequest) {
        this.taskId = jobRequest.getTaskId();
        this.senderId = jobRequest.getSenderId();
        this.jobName = jobRequest.getJobName();
        this.jobGroup = jobRequest.getJobGroup();
        this.status = "Running";
        this.delayTime = jobRequest.getDelayTime();
        this.executeCount = jobRequest.getExecuteCount();
        this.intervalTime = jobRequest.getIntervalTime();
        this.cronExpression = jobRequest.getCronExpression();
        this.receives = jobRequest.getReceivers();
        this.subject = jobRequest.getSubject();
        this.context = jobRequest.getContext();
        this.attachment_path = jobRequest.getAttachmentPath();
        this.isClear = jobRequest.isClear();
    }

    @Id
    @Column(name = "task_id")
    private String taskId;

    @Column(name = "sender_id")
    private String senderId;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "job_group")
    private String jobGroup;

    @Column(name = "status")
    private String status;

    @Column(name = "delay_time")
    private Long delayTime;

    @Column(name = "execute_count")
    private Integer executeCount;

    @Column(name = "interval_time")
    private Long intervalTime;

    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(name = "receives")
    private String receives;

    @Column(name = "subject")
    private String subject;

    @Column(name = "context")
    private String context;

    @Column(name = "attachment_path")
    private String attachment_path;

    @Column(name = "is_clear")
    private boolean isClear;

}
