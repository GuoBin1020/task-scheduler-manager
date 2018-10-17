package com.example.tsm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JobRequest {

    // 发送人ID
    @JsonProperty("senderId")
    private String senderId;
    // 任务名称
    @JsonProperty("jobName")
    private String jobName;
    // 任务组
    @JsonProperty("jobGroup")
    private String jobGroup;
    // 接收人邮箱
    @JsonProperty("receivers")
    private String receivers;
    // 邮箱主题
    @JsonProperty("subject")
    private String subject;
    // 邮箱内容
    @JsonProperty("context")
    private String context;
    // 附件路径
    @JsonProperty("attachmentPath")
    private String attachmentPath;
    // 发送完成后是否清除附件,默认不清除
    @JsonProperty("isClear")
    private boolean isClear = false;
    // 执行次数，默认-1，无限次
    @JsonProperty("executeCount")
    private Integer executeCount = -1;
    // 配置完后延迟执行，默认为0不延迟，单位毫秒
    @JsonProperty("delayTime")
    private Long delayTime = 0L;
    // 每轮间隔时间，单位毫秒
    @JsonProperty("intervalTime")
    private Long intervalTime;
    // 表达式
    @JsonProperty("cronExpression")
    private String cronExpression;

    @JsonIgnore
    public String getTaskId() {
        return jobName + "." + jobGroup;
    }

}
