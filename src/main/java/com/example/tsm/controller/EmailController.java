package com.example.tsm.controller;

import com.example.tsm.model.JobRequest;
import com.example.tsm.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/job/email")
@Slf4j
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * 添加Job,使用thymeleaf模板不能使用@RequestBody注解，这个注解一般用于rest请求
     * @param request 请求
     * @param model model
     * @return 跳转页面
     * @throws Exception 异常
     */
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public String addJob(@ModelAttribute JobRequest request, Model model) throws Exception {
        try {
            emailService.addOrUpdateJob(request);
            model.addAttribute("result", "Success");
            return "redirect:/index";
        } catch (Exception e) {
            log.error("添加失败！！！", e);
            throw e;
        }
    }

    @RequestMapping(path = "/pause", method = RequestMethod.GET)
    public String pauseJob(@RequestParam("taskId") String taskId, Model model) throws Exception {
        try {
            emailService.pauseJob(taskId);
            model.addAttribute("result", "Success");
            return "redirect:/index";
        } catch (Exception e) {
            log.error("暂停失败！！！", e);
            throw e;
        }
    }

    @RequestMapping(path = "/resume", method = RequestMethod.GET)
    public String resumeJob(@RequestParam("taskId") String taskId, Model model) throws Exception {
        try {
            emailService.resumeJob(taskId);
            model.addAttribute("result", "Success");
            return "redirect:/index";
        } catch (Exception e) {
            log.error("恢复失败！！！", e);
            throw e;
        }
    }

    @RequestMapping(path = "/delete", method = RequestMethod.GET)
    public String deleteJob(@RequestParam("taskId") String taskId, Model model) throws Exception {
        try {
            emailService.deleteJob(taskId);
            model.addAttribute("result", "Success");
            return "redirect:/index";
        } catch (Exception e) {
            log.error("删除失败！！！", e);
            throw e;
        }
    }
}
