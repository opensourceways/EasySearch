package com.search.docsearch.utils;

import com.search.docsearch.config.EsfunctionScoreConfig;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

public class ParameterUtil {
    public static void vailAndLimitRequestMap(Map<String, String> condition,String esExistingKey) {
        if (CollectionUtils.isEmpty(condition))
            throw new IllegalArgumentException("Invalid request param");
        String page = vaildPage(condition.get("page"));
        String pageSize = vaildPageSize(condition.get("pageSize"));
        String lang = vaildLang(condition.get("lang"));
        for (Iterator<Map.Entry<String, String>> it = condition.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, String> item = it.next();
            String key = item.getKey();
            if (!key.matches("^[\\x20\\u4E00-\\u9FA5A-Za-z0-9.\\-_ ]+$") || key.length() > 15) {
                throw new IllegalArgumentException("Invalid key");
            }
            if (!condition.get(key).matches("^[\\x20\\u4E00-\\u9FA5A-Za-z0-9.\\-_ ]+$")
                    || condition.get(key).length() > 20) {
                throw new IllegalArgumentException("Invalid value");
            }
            if (!StringUtils.isEmpty(esExistingKey)&&!esExistingKey.contains(key)) {
                it.remove();
            }
        }
        condition.put("lang", lang);
        condition.put("page", page);
        condition.put("pageSize", pageSize);
    }


    public static void vaildMap(Map<String, String> condition) {
        if (condition != null && condition.size() > 0) {
            for (String key : condition.keySet()) {
                if (!key.matches("^[\\x20\\u4E00-\\u9FA5A-Za-z0-9.\\-_ ]+$") || key.length() > 30) {
                    throw new IllegalArgumentException("Invalid key");
                }
                if (!condition.get(key).matches("^[\\x20\\u4E00-\\u9FA5A-Za-z0-9.\\-_ ]+$")
                        || condition.get(key).length() > 30) {
                    throw new IllegalArgumentException("Invalid value");
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

    public static String vaildLang(String lang) {
        lang = lang == null ? "zh" : lang;
        if (!lang.equalsIgnoreCase("zh") && !lang.equalsIgnoreCase("en")) {
            throw new IllegalArgumentException("Invalid lang parameter");
        }
        return lang.toLowerCase(Locale.ROOT);
    }

    public static String vaildCommunity(String community) {
        if (!"mindspore".equalsIgnoreCase(community) && !"opengauss".equalsIgnoreCase(community)
                && !"openlookeng".equalsIgnoreCase(community) && !"xihe".equalsIgnoreCase(community)) {
            throw new IllegalArgumentException("Invalid community parameter");
        }
        return community.toLowerCase(Locale.ROOT);
    }

    public static String vaildEcosystemType(String ecosystemType) {
        ecosystemType = ecosystemType == null ? "Library" : ecosystemType;
        if (!"Library".equalsIgnoreCase(ecosystemType) && !"Tutorial".equalsIgnoreCase(ecosystemType) && !"Model".equalsIgnoreCase(ecosystemType)) {
            throw new IllegalArgumentException("Invalid ecosystemType parameter");
        }
        return ecosystemType;
    }

    public static String vaildPage(String page) {
        page = page == null ? "1" : page;
        if (Integer.parseInt(page) > 100 || Integer.parseInt(page) < 1) {
            throw new IllegalArgumentException("Invalid page parameter");
        }
        return page;
    }

    public static String vaildPageSize(String pageSize) {
        pageSize = pageSize == null ? "10" : pageSize;
        if (Integer.parseInt(pageSize) > 50 || Integer.parseInt(pageSize) < 5) {
            throw new IllegalArgumentException("Invalid vaildPageSize parameter");
        }
        return pageSize;
    }

}
