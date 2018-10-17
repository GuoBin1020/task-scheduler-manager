package com.example.tsm.cache;

import com.example.tsm.dao.SenderRepository;
import com.example.tsm.entity.SenderEntity;
import com.example.tsm.utils.SpringIOCUtil;

import java.util.concurrent.ConcurrentHashMap;

public class SenderCache {

    private static final ConcurrentHashMap<String, SenderEntity> senderMap = new ConcurrentHashMap<>();

    public static SenderEntity get(String senderId) {
        SenderRepository senderRepository = (SenderRepository) SpringIOCUtil.getBeanByName("senderRepository");
        if (senderMap.get(senderId) == null && senderRepository != null) {
            senderRepository.findById(senderId).ifPresent(senderEntity -> senderMap.put(senderId, senderEntity));
        }
        return senderMap.get(senderId);
    }

    public synchronized static void put(String senderId, SenderEntity senderEntity) {
        senderMap.put(senderId, senderEntity);
    }

}
