package com.example.tsm.controller;

import com.example.tsm.entity.SenderEntity;
import com.example.tsm.entity.TimedTaskEntity;
import com.example.tsm.job.SenderEmailTimer;
import com.example.tsm.model.JobRequest;
import com.example.tsm.service.SenderService;
import com.example.tsm.service.TimedTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(path = "/")
public class RouteController {

    private final TimedTaskService timedTaskService;
    private final SenderService senderService;

    @Autowired
    public RouteController(TimedTaskService timedTaskService, SenderService senderService) {
        this.timedTaskService = timedTaskService;
        this.senderService = senderService;
    }

    @RequestMapping(path="/index", method = RequestMethod.GET)
    public String rIndex(Model model) {
        List<TimedTaskEntity> timedTaskEntities = timedTaskService.findAllTimedTask();
        model.addAttribute("timedTasks", timedTaskEntities);
        return "index";

    }

    @RequestMapping(path="/create", method = RequestMethod.GET)
    public String rCreate(Model model) {
        List<SenderEntity> senderEntities = senderService.findAllSender();
        model.addAttribute("senders", senderEntities);
        model.addAttribute("request", new JobRequest());
        return "create-job";

    }

}
