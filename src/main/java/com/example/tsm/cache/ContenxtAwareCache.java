package com.example.tsm.cache;

import java.util.concurrent.ConcurrentHashMap;

public class ContenxtAwareCache {

    private static final ConcurrentHashMap<String, Object> contextAwareMap = new ConcurrentHashMap<>();

    public static Object get(String className) {
        return contextAwareMap.get(className);
    }

    public synchronized static void put(String className, Object obj) {
        contextAwareMap.put(className, obj);
    }

}
