package com.example.tsm.service;

import com.example.tsm.dao.TimedTaskRepository;
import com.example.tsm.entity.TimedTaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimedTaskService {

    private final TimedTaskRepository timedTaskRepository;

    @Autowired
    public TimedTaskService(TimedTaskRepository timedTaskRepository) {
        this.timedTaskRepository = timedTaskRepository;
    }

    public List<TimedTaskEntity> findAllTimedTask() {
        return timedTaskRepository.findAll();
    }
}
