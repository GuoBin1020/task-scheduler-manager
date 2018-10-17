package com.example.tsm.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SpringIOCUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static Object getBeanByName(String beanName) {
        try {
            return applicationContext.getBean(beanName);
        } catch (Exception e) {
            log.error("获得注入对象失败！！！", e);
        }
        return null;
    }
}
