package com.piotrpabich.talktactics.common.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    private final static JsonMapper objectMapper = JsonMapper.builder()
            .enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES)
            .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
            .enable(JsonReadFeature.ALLOW_MISSING_VALUES)
            .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .visibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
            .addModule(new JavaTimeModule())
            .addModule(new Jdk8Module())
            .build();
    public static Map<String, String> getJsonPropertyFieldMap(Class<?> clazz) {
        Map<String, String> fieldMap = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            JsonProperty annotation = field.getAnnotation(JsonProperty.class);
            if (annotation != null) {
                fieldMap.put(annotation.value(), field.getName());
            } else {
                fieldMap.put(field.getName(), field.getName());
            }
        }
        return fieldMap;
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static String toJson(Object obj) throws Exception {
        return getObjectMapper().writeValueAsString(obj);
    }
}
