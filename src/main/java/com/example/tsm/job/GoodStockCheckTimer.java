package com.example.tsm.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public class GoodStockCheckTimer extends QuartzJobBean {

    /**
     * logback
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodStockCheckTimer.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("执行库存检查定时任务，执行时间：{}",new Date());
    }

}
