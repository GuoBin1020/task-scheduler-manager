package com.example.tsm.cache;

import com.example.tsm.entity.SenderEntity;

import java.util.concurrent.ConcurrentHashMap;

public class SenderCache {

    private static final ConcurrentHashMap<String, SenderEntity> senderMap = new ConcurrentHashMap<>();

    public static SenderEntity get(String senderId) {
        return senderMap.get(senderId);
    }

    public synchronized static void put(String senderId, SenderEntity senderEntity) {
        senderMap.put(senderId, senderEntity);
    }

}
