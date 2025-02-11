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
package com.search.docsearch.multirecall.recall.cstrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.HtmlUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.search.docsearch.constant.Constants;
import com.search.docsearch.entity.vo.GoogleSearchParams;
import com.search.docsearch.entity.vo.SearchCondition;
import com.search.docsearch.except.ServiceImplException;
import com.search.docsearch.factorys.HttpConnectFactory;
import com.search.docsearch.multirecall.composite.Component;
import com.search.docsearch.multirecall.composite.cdata.GRecallData;
import com.search.docsearch.multirecall.recall.SearchStrategy;
import com.search.docsearch.properties.GoogleSearchProperties;

public class GSearchStrategy implements SearchStrategy {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GSearchStrategy.class);

    /**
     * insert google serach properties
     */
    private GoogleSearchProperties gProperties;

    /**
     * insert httpConnectionFactory to creat a URL
     */
    private HttpConnectFactory httpConnectFactory;

   /**
     * jieba segmenter 
     */
    private JiebaSegmenter segmenter;

    public GSearchStrategy(GoogleSearchProperties gProperties, HttpConnectFactory httpConnectFactory) {
        this.gProperties = gProperties;
        this.httpConnectFactory = httpConnectFactory;
        this.segmenter = new JiebaSegmenter();
    }

    /**
     * roughly filter the recalled results
     * 
     * @param SearchCondition paraClient mannage by spring aoc
     */
    @Override
    public Component search(SearchCondition condition) {
        //writing google search logic here
        GRecallData emptyRes = new GRecallData(Collections.emptyMap()); // 空返回
        try {
            Component GRecallData = searchByCondition(condition);
            return GRecallData == null ? emptyRes : GRecallData; // 遇到正常0召回情况，返回空结果
        } catch (Exception e) {
            LOGGER.error("google search result error :{}", e.getMessage());
            return emptyRes;
        }
    }

    /**
     * doing the recall according user query 
     * 
     * @param SearchCondition the user query
     * @return a Component containing the parsed response data from the Search API
     * @throws ServiceImplException
     * @throws IOException
     */
    private Component searchByCondition(SearchCondition condition) throws ServiceImplException, IOException {
        if (!"".equals(condition.getType())) {
            return null;
        }
        // google search 处理无效字符
        condition.setKeyword(condition.getKeyword().replace(" ", ""));
        condition.setKeyword(condition.getKeyword().replace(".", ""));
        GoogleSearchParams googleSearchParams = new GoogleSearchParams();
        googleSearchParams.setKeyWord(condition.getKeyword());
        if ("en".equals(condition.getLang())) {
            googleSearchParams.setLr("lang_en");
        }
        int start = (condition.getPage() - 1) * condition.getPageSize() + 1;
        int num = Constants.GOOGLE_NUM;
        if(start + num > 100) {
            return null;
        } else {
            googleSearchParams.setNum(String.valueOf(num));
            googleSearchParams.setStart(String.valueOf(start));
        }
        int count = 0;
        String keyWord = googleSearchParams.getKeyWord();
        String urlString = googleSearchParams.buildUrl(gProperties.getUrl(), gProperties.getKey(), gProperties.getCx(), gProperties.getHq());
        // 创建connection对象
        HttpURLConnection connection = httpConnectFactory.createConnection(urlString);
        try {
            connection.setRequestMethod("GET");
            int timeout = 15000; // 设置超时时间为15秒
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            int responseCode = connection.getResponseCode();
            // 如果响应成功（状态码200-299），则读取响应体
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    Map<String, Object> result = new HashMap<>();
                    result.put("keyword", HtmlUtils.htmlEscape(keyWord));
                    ObjectMapper mapper = new ObjectMapper();
                    List<Map<String, Object>> data = new ArrayList<>();
                    JsonNode rootNode = mapper.readTree(response.toString());
                    JsonNode termsNode = rootNode.get("items");
                    if (termsNode.isArray()) {
                        for (JsonNode termNode : termsNode) {
                            Map<String, Object> map = new HashMap<>();
                            String highlightTittle = highLightContent(condition.getKeyword(),termNode.get("title").asText());
                            String highlightText = highLightContent(condition.getKeyword(),termNode.get("snippet").asText());
                            map.put("title", highlightTittle);
                            String path = termNode.get("link").asText();
                            String type = parseTypeByPath(path);
                            map.put("type", type);
                            path = reHandlePath(path, type);
                            map.put("path", path);
                            map.put("textContent", highlightText);
                            if ("lang_en".equals(googleSearchParams.getLr())) {
                                map.put("lang", "en");
                            } else {
                                map.put("lang", "zh");
                            }
                            map.put("score", (double) (5000 - (count + start) * 50));
                            map.put("recallType","G");
                            count++;
                            data.add(map);
                        }
                    }
                    result.put("records", data);
                    GRecallData resData = new GRecallData(result);
                    return resData;
                }
            } else {
                LOGGER.error("GET request not worked, response code: {}", responseCode);
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("google search error: {}", e.getMessage());
        } finally {
            connection.disconnect();
        }
        return null;
    }

    /**
     * doing the recall according user query 
     * 
     * @param searchkey the user query
     * @param content the text contnt
     * @return text content with highlight
     */
    public String highLightContent(String searchkey, String content){
        List<String> segments = this.segmenter.sentenceProcess(searchkey);
        String lightContent = content;
        for (String keyword : segments){
            Pattern pattern = Pattern.compile(Pattern.quote(keyword), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(lightContent);
            StringBuffer result = new StringBuffer();
            while (matcher.find()) {
                matcher.appendReplacement(result, "<span>" + matcher.group() + "</span>");
            }
            matcher.appendTail(result);
            lightContent = result.toString();
        }
        return lightContent;
    }

    /**
     * parse google  path to a type params
     * 
     * @param path the google search link
     * @return type content string
     */
    public String parseTypeByPath(String path){
        String type = "other";
        HashSet<String> hashSet = new HashSet<>(gProperties.getTypeList());
        String flag = "zh/";
        if (path.indexOf(flag) == -1) {
            flag = "en/";
        }
        String[] spliteArray = path.split(flag);
        if (spliteArray.length < 2) {
            if (path.indexOf("/t/") != -1 && path.indexOf("forum") != -1) {
                type = "forum";
            }
            return type;
        } else {
            int index = spliteArray[1].indexOf("/");
            if (index != -1 && hashSet.contains(spliteArray[1].substring(0, index))) {
                type = spliteArray[1].substring(0, index);
            } else {
                return type;
            }
        }
        return type;
    }

     /**
     * parse google  path to a type params
     * 
     * @param path the google search link
     * @param type the google content type
     * @return type content string
     */
    public String reHandlePath(String path, String type){
        path = path.replace("http:", "https:");
        if ("docs".equals(type) || "news".equals(type) || "blog".equals(type)) {
            String flag = "zh/";
            if (path.indexOf(flag) == -1) {
                flag = "en/";
            }
            int index = path.indexOf(flag);
            path = path.substring(index);
            path = path.replaceAll(".html", "");
        } else if ("forum".equals(type)) {
            String flag = "t/";
            int index = path.indexOf(flag);
            path = path.substring(index);
        }
        return path;
    }
}