package com.example.tsm.controller;

import com.example.tsm.model.JobInfo;
import com.example.tsm.wrapper.JobWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/job")
public class JobController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobController.class);

    private final JobWrapper jobWrapper;

    @Autowired
    public JobController(JobWrapper jobWrapper) {
        this.jobWrapper = jobWrapper;
    }

    /**
     * 添加任务
     * @param jobInfo 定时任务信息
     * @return 结果
     * @throws Exception 异常
     */
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public String addJob(@RequestBody JobInfo jobInfo) throws Exception {
        try {
            jobWrapper.addJob(jobInfo);
            return "添加任务成功!!!";
        } catch (Exception e) {
            LOGGER.error("添加任务失败!!!", e);
            throw e;
        }
    }

    /**
     * 暂停任务
     * @param jobInfo 定时任务信息
     * @return 结果
     * @throws Exception 异常
     */
    @RequestMapping(path = "/pause", method = RequestMethod.POST)
    public String pauseJob(@RequestBody JobInfo jobInfo) throws Exception {
        try {
            jobWrapper.pauseJob(jobInfo);
            return "暂停任务成功!!!";
        } catch (Exception e) {
            LOGGER.error("暂停任务失败!!!", e);
            throw e;
        }
    }

    /**
     * 恢复任务
     * @param jobInfo 定时任务信息
     * @return 结果
     * @throws Exception 异常
     */
    @RequestMapping(path = "/resume", method = RequestMethod.POST)
    public String resumeJob(@RequestBody JobInfo jobInfo) throws Exception {
        try {
            jobWrapper.resumeJob(jobInfo);
            return "恢复任务成功!!!";
        } catch (Exception e) {
            LOGGER.error("恢复任务失败!!!", e);
            throw e;
        }
    }

}
