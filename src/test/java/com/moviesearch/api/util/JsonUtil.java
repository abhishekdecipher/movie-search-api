package com.moviesearch.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviesearch.api.exception.SearchException;

import java.io.IOException;

public class JsonUtil {

    public static String toJson(final Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new SearchException(e);
        }
    }

    public static <T> T fromJson(final String json, final Class<T> clazz) {
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new SearchException(e);
        }
    }
}
