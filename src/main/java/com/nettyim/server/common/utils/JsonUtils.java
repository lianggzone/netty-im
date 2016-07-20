package com.nettyim.server.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * <p>Title: JSON工具类  </p>
 * <p>Description: Function Description </p>
 * <p>Copyright: Copyright (c) 2016     </p>
 * @author lianggz
 */
public class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String toJson(Object obj) throws JsonProcessingException {
        return MAPPER.writeValueAsString(obj);
    }

    public static <T> T fromJson(byte[] json, Class<T> clazz) throws IOException {
        return MAPPER.readValue(json, clazz);
    }
}
