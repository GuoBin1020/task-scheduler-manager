package com.example.tsm.controller;

import com.example.tsm.model.JobRequest;
import com.example.tsm.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/job/email")
@Slf4j
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public String addJob(@RequestBody JobRequest request, Model model) throws Exception {
        try {
            emailService.addOrUpdateJob(request);
            model.addAttribute("name", "thymeleaf");
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
