package com.example.tsm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class TaskScheduleManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskScheduleManagerApplication.class, args);
    }

    @Component
    @Order(1)
    @Slf4j
    static class InitClass implements CommandLineRunner {
        @Override
        public void run(String... args) {
//            log.info("缓存上下文对象完成！！");
//            // 缓存发送人
//            List<SenderEntity> senderEntities = senderRepository.findAll();
//            if (!CollectionUtils.isEmpty(senderEntities)) {
//                for (SenderEntity senderEntity : senderEntities) {
//                    SenderCache.put(senderEntity.getSenderId(), senderEntity);
//                }
//            }
//            log.info("发送人缓存完成！！");
        }
    }

}
