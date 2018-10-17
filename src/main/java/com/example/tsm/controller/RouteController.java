package com.example.tsm.controller;

import com.example.tsm.entity.TimedTaskEntity;
import com.example.tsm.service.TimedTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(path = "/index")
public class RouteController {

    private final TimedTaskService timedTaskService;

    @Autowired
    public RouteController(TimedTaskService timedTaskService) {
        this.timedTaskService = timedTaskService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String test(Model model) {
        List<TimedTaskEntity> timedTaskEntities = timedTaskService.findAllTimedTask();
        model.addAttribute("timedTasks", timedTaskEntities);
        return "index";

    }

}
