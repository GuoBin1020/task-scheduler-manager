package com.example.tsm;

import com.example.tsm.cache.SenderCache;
import com.example.tsm.dao.SenderRepository;
import com.example.tsm.entity.SenderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@SpringBootApplication
public class TaskScheduleManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskScheduleManagerApplication.class, args);
    }

    @Component
    @Order(1)
    @Slf4j
    static class InitClass implements CommandLineRunner {

        /**
         * 发送人数据接口
         */
        private final SenderRepository senderRepository;

        public InitClass(SenderRepository senderRepository) {
            this.senderRepository = senderRepository;
        }

        @Override
        public void run(String... args) throws Exception {
            List<SenderEntity> senderEntities = senderRepository.findAll();
            if (!CollectionUtils.isEmpty(senderEntities)) {
                for (SenderEntity senderEntity : senderEntities) {
                    SenderCache.put(senderEntity.getSenderId(), senderEntity);
                }
            }
            log.info("发送人缓存完成！！");
        }
    }

}
