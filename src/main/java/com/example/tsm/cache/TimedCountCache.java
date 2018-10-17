package com.example.tsm.cache;

import java.util.concurrent.ConcurrentHashMap;

public class TimedCountCache {

    private static final ConcurrentHashMap<String, Integer> timedCountMap = new ConcurrentHashMap<>();

    public static Integer get(String taskId) {
        return timedCountMap.get(taskId);
    }

    public synchronized static void put(String taskId, Integer count) {
        timedCountMap.put(taskId, count);
    }

}
