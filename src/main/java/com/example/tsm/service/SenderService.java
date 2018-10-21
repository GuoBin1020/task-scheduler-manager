package com.example.tsm.service;

import com.example.tsm.dao.SenderRepository;
import com.example.tsm.entity.SenderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SenderService {

    private final SenderRepository senderRepository;

    @Autowired
    public SenderService(SenderRepository senderRepository) {
        this.senderRepository = senderRepository;
    }

    public List<SenderEntity> findAllSender() {
        return senderRepository.findAll();
    }

}
