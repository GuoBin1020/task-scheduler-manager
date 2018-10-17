package com.example.tsm.model;

import lombok.Data;

@Data
public class JobRequest {

    private String senderId;
    private String jobName;
    private String jobGroup;
    private String receivers;
    private String subject;
    private String context;
    private String cronExpression;

}
