package com.search.docsearch.utils;

import java.util.List;
import java.util.Map;

public class MapUtil {
    public static void vaildMap(Map<String, String> condition) {
        if (condition != null && condition.size() > 0) {
            for (String key : condition.keySet()) {
                if (!key.matches("^[a-zA-Z]+$")) {
                    throw new IllegalArgumentException("invalid key");
                }
                if (!condition.get(key).matches("^[\\u4E00-\\u9FA5A-Za-z0-9.\\-_]+$")) {
                    throw new IllegalArgumentException("invalid value");
                }
            }
        }
    }

    public static void vaildListMap(List<Map<String, String>> conditionList) {
        if (conditionList != null) {
            for (Map<String, String> condition : conditionList) {
                vaildMap(condition);
            }
        }
    }
}
