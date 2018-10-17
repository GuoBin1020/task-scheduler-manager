package com.example.tsm.controller;

import com.example.tsm.model.JobRequest;
import com.example.tsm.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/job/email")
@Slf4j
public class EmailController {

    private final EmailService emailService;


    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public String addJob(@RequestBody JobRequest request) throws Exception {
        try {
            emailService.addJob(request);
            return "Success";
        } catch (Exception e) {
            log.error("添加失败！！！", e);
            throw e;
        }
    }

}
