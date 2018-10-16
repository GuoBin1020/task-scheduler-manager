package com.example.tsm.utils;

import org.quartz.Job;

public class JobUtil {

    private JobUtil() {}

    public static Job getJob(String jobClassName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> clazz = Class.forName(jobClassName);
        return (Job) clazz.newInstance();
    }

}
