package com.example.tsm.service;

import com.example.tsm.dao.GoodInfoRepository;
import com.example.tsm.entity.GoodInfoEntity;
import com.example.tsm.job.GoodAddTimer;
import com.example.tsm.job.GoodStockCheckTimer;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class GoodInfoService {

    /**
     * 注入任务调度器
     */
    private final Scheduler scheduler;
    /**
     * 商品数据接口
     */
    private final GoodInfoRepository goodInfoRepository;

    @Autowired
    public GoodInfoService(@Qualifier("Scheduler") Scheduler scheduler, GoodInfoRepository goodInfoRepository) {
        this.scheduler = scheduler;
        this.goodInfoRepository = goodInfoRepository;
    }

    /**
     * 保存商品基本信息
     *
     * @param good 商品实例
     * @return
     */
    public Long saveGood(GoodInfoEntity good) throws Exception {
        goodInfoRepository.save(good);
        //构建创建商品定时任务
        buildCreateGoodTimer();
        //构建商品库存定时任务
        buildGoodStockTimer();
        return good.getId();
    }

    /**
     * 构建创建商品定时任务
     */
    public void buildCreateGoodTimer() throws Exception {
        //设置开始时间为1分钟后
        long startAtTime = System.currentTimeMillis() + 1000 * 60;
        //任务名称
        String name = UUID.randomUUID().toString();
        //任务所属分组
        String group = GoodAddTimer.class.getName();
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(GoodAddTimer.class).withIdentity(name, group).build();
        //创建任务触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).startAt(new Date(startAtTime)).build();
        //将触发器与任务绑定到调度器内
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 构建商品库存定时任务
     * @throws Exception
     */
    public void buildGoodStockTimer() throws Exception
    {
        //任务名称
        String name = UUID.randomUUID().toString();
        //任务所属分组
        String group = GoodStockCheckTimer.class.getName();

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/30 * * * * ?");
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(GoodStockCheckTimer.class).withIdentity(name,group).build();
        //创建任务触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name,group).withSchedule(scheduleBuilder).build();
        //将触发器与任务绑定到调度器内
        scheduler.scheduleJob(jobDetail, trigger);
    }

}
