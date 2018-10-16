package com.example.tsm.model;

public class JobInfo {

    private String jobId;
    private String jobName;
    private String jobClassName;
    private String jobGroupName;
    private String cronExpression;
    private String status;

    public String getJobId() {
        return jobId;
    }

    public JobInfo setJobId(String jobId) {
        this.jobId = jobId;
        return this;
    }

    public String getJobName() {
        return jobName;
    }

    public JobInfo setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public JobInfo setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
        return this;
    }

    public String getJobGroupName() {
        return jobGroupName;
    }

    public JobInfo setJobGroupName(String jobGroupName) {
        this.jobGroupName = jobGroupName;
        return this;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public JobInfo setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public JobInfo setStatus(String status) {
        this.status = status;
        return this;
    }
}
