/* Copyright (c) 2024 openEuler Community
 EasySoftware is licensed under the Mulan PSL v2.
 You can use this software according to the terms and conditions of the Mulan PSL v2.
 You may obtain a copy of Mulan PSL v2 at:
     http://license.coscl.org.cn/MulanPSL2
 THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 See the Mulan PSL v2 for more details.
*/

package com.search.common.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.search.common.exception.MyJacksonException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Jackson工具类.
 */
public final class ObjectMapperUtil {

    /**
     * ObjectMapper instance for JSON serialization and deserialization.
     */
    private static ObjectMapper objectMapper = null;

    // Private constructor to prevent instantiation of the utility class
    private ObjectMapperUtil() {
        // private constructor to hide the implicit public one
        throw new AssertionError("ObjectMapperUtil class cannot be instantiated.");
    }

    static {
        objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(
                LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(
                LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(
                LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.registerModule(javaTimeModule);
        // 设置时区
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        // 忽略空bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 设置输入时忽略JSON字符串中存在而Java对象实际没有的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Convert an object to its JSON representation as a string.
     *
     * @param obj The object to convert
     * @return The JSON representation of the object as a string
     */
    public static String writeValueAsString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException var2) {
            throw new MyJacksonException("JSON 转化异常！");
        }
    }

    /**
     * Convert a JSON string to an object of the specified type.
     *
     * @param <T>       Type parameter for the method.
     * @param json      The JSON string to convert
     * @param valueType The class of the object to convert to
     * @return An object of the specified type
     */
    public static <T> T jsonToObject(final String json, final Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (Exception e) {
            throw new MyJacksonException("字符串转化Java对象时异常");
        }
    }

    /**
     * Convert an object to its JSON representation as a string, handling null values.
     *
     * @param obj The object to convert
     * @return The JSON representation of the object as a string, including null values
     */
    public static String writeValueAsStringForNull(final Object obj) {
        objectMapper
                .getSerializerProvider()
                .setNullValueSerializer(
                        new JsonSerializer<Object>() {
                            @Override
                            public void serialize(Object arg0, JsonGenerator arg1, SerializerProvider arg2)
                                    throws IOException, JsonProcessingException {
                                arg1.writeString("");
                            }
                        });

        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException var2) {
            throw new MyJacksonException("JSON 转化异常！");
        }
    }

    /**
     * Convert an object to its JSON representation as a byte array.
     *
     * @param obj The object to convert
     * @return The JSON representation of the object as a byte array
     */
    public static byte[] toJsonByte(final Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (Exception var2) {
            throw new MyJacksonException("将对象转换为JSON字符串二进制数组错误！！");
        }
    }

    /**
     * Convert a JSON string to a Map&lt;String, Object&gt;.
     *
     * @param json The JSON string to convert
     * @return A map representing the JSON data
     */
    public static Map<String, Object> toMap(final String json) {
        try {
            return (Map) objectMapper.readValue(json, Map.class);
        } catch (Exception var2) {
            throw new MyJacksonException("字符串转为map异常！！");
        }
    }

    /**
     * Convert an object to its JSON representation as an OutputStream.
     *
     * @param obj The object to convert
     * @return The JSON representation of the object as an OutputStream
     */
    public static OutputStream toJsonOutStream(final Object obj) {
        try {
            OutputStream os = new ByteArrayOutputStream();
            objectMapper.writeValue(os, obj);
            return os;
        } catch (Exception var2) {
            throw new MyJacksonException("无法转化为字符串流！！");
        }
    }

    /**
     * Convert a JSON string to an object of the specified class.
     *
     * @param <T> Type parameter for the method.
     * @param clazz The class of the object to convert to
     * @param json  The JSON string to convert
     * @return An object of the specified class
     */
    public static <T> T toObject(final Class<T> clazz, final String json) {
        T obj = null;

        try {
            if (json == null) {
                return null;
            } else {
                obj = objectMapper.readValue(json, clazz);
                return obj;
            }
        } catch (Exception var4) {
            throw new MyJacksonException("json字符串转化错误！！");
        }
    }

    /**
     * Convert a byte array representing JSON to an object of the specified class.
     *
     * @param <T> Type parameter for the method.
     * @param clazz The class of the object to convert to
     * @param bytes The byte array representing JSON data
     * @return An object of the specified class
     */
    public static <T> T toObject(final Class<T> clazz, final byte[] bytes) {
        T obj = null;

        try {
            if (bytes != null && bytes.length != 0) {
                obj = objectMapper.readValue(bytes, clazz);
                return obj;
            } else {
                return null;
            }
        } catch (Exception var4) {
            throw new MyJacksonException("二进制转化错误！！");
        }
    }

    /**
     * Convert a JSON string to a list of objects of the specified class.
     *
     * @param <T> Type parameter for the method.
     * @param clazz The class of the objects in the list
     * @param json  The JSON string to convert
     * @return A list of objects of the specified class
     */
    public static <T> List<T> toObjectList(final Class<T> clazz, final String json) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, clazz);
            return objectMapper.readValue(json, javaType);
        } catch (IOException var3) {
            throw new MyJacksonException("json字符串转为list异常！！");
        }
    }

    /**
     * Convert a list of ConcurrentHashMap containing String keys and Object values to a JSON tree,
     * optionally appending additional objects as count.
     *
     * @param pageMap The list of ConcurrentHashMap to convert
     * @param count   Additional objects to append
     * @return The JSON representation of the data
     */
    public static String toJsonTree(final List<ConcurrentHashMap<String, Object>> pageMap, final Object... count) {
        List<ConcurrentHashMap<String, Object>> myMap = new ArrayList<>();
        for (ConcurrentHashMap<String, Object> map : pageMap) {
            ConcurrentHashMap<String, Object> tempMap = new ConcurrentHashMap<>();
            String key;
            Object value;
            for (Iterator<String> iterator = map.keySet().iterator();
                 iterator.hasNext();
                 tempMap.put(key.toLowerCase(), value)) {
                key = iterator.next();
                value = map.get(key);
                if ("parentid".equals(key)) {
                    tempMap.put("_parentId", value);
                }
            }
            myMap.add(tempMap);
        }
        Map<String, Object> jsonMap = new HashMap<>(2);
        jsonMap.put("total", count);
        jsonMap.put("rows", myMap);
        try {
            return objectMapper.writeValueAsString(jsonMap);
        } catch (JsonProcessingException var9) {
            throw new MyJacksonException("转换json树异常！！");
        }
    }

    /**
     * Converts the input content to a JsonNode.
     *
     * @param content The content to convert to JsonNode
     * @return JsonNode representing the input content
     */
    public static JsonNode toJsonNode(final String content) {
        try {
            return objectMapper.readTree(content);
        } catch (JsonProcessingException var2) {
            throw new MyJacksonException("JSON 转化异常！");
        }
    }

    /**
     * Convert a JSON string to a JsonNode object.
     *
     * @param <T> Type parameter for the method.
     * @param obj The JSON string to convert
     * @return The JsonNode representation of the JSON data
     */
    public static <T> Map<String, T> jsonToMap(final Object obj) {
        return objectMapper.convertValue(obj, new TypeReference<Map<String, T>>() {
        });
    }
}
