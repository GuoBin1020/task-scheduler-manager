package com.example.tsm.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JSONUtil {

    private JSONUtil() {}

    private static final ObjectMapper om = new ObjectMapper();

    public static String toJson(Object obj) {
        try {
            return om.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转JSON失败！！！", e);
        }
        return "";
    }

    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        try {
            return om.readValue(jsonStr, clazz);
        } catch (IOException e) {
            log.error("JSON转对象失败！！！", e);
        }
        return null;
    }

}
