package com.example.tsm.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendEmailJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendEmailJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("工作进行中...");
    }

}
