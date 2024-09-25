package com.search.infrastructure.openapi.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.search.adapter.condition.NpsConditon;
import com.search.common.thread.ThreadLocalCache;
import com.search.common.util.ParameterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.StringJoiner;

@Component
public class JumperGatewayImpl {
    /**
     * allApi.
     */
    @Value("${api.allApi}")
    private String allApi;
    /**
     * starsApi.
     */
    @Value("${api.starsApi}")
    private String starsApi;
    /**
     * sigNameApi.
     */
    @Value("${api.sigNameApi}")
    private String sigNameApi;
    /**
     * repoInfoApi.
     */
    @Value("${api.repoInfoApi}")
    private String repoInfoApi;
    /**
     * npsApi.
     */
    @Value("${api.npsApi}")
    private String npsApi;


    /**
     * Logger instance for JumperGatewayImpl.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JumperGatewayImpl.class);

    /**
     * jump request.
     *
     * @param lang 语言 .
     * @return String.
     */
    public String querySigName(String lang) {
        String community = ThreadLocalCache.getDataSource();
        String urlStr = String.format(Locale.ROOT, sigNameApi, community, lang);
        String res = null;
        try {
            res = httpRequest(urlStr, false);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return res;
    }

    /**
     * jump request.
     *
     * @return String.
     */
    public String queryAll() {
        String community = ThreadLocalCache.getDataSource();
        String urlStr = String.format(Locale.ROOT, allApi, community);
        String res = null;
        try {
            res = httpRequest(urlStr, false);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return res;
    }

    /**
     * jump request.
     *
     * @return String.
     */
    public String queryStars() {
        String community = ThreadLocalCache.getDataSource();
        String urlStr = String.format(Locale.ROOT, starsApi, community);
        String res = null;
        try {
            res = httpRequest(urlStr, false);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return res;
    }

    /**
     * jump request.
     *
     * @param lang 语言 .
     * @param sig  sig .
     * @return String.
     */
    public String querySigReadme(String sig, String lang) {
        lang = ParameterUtil.vaildLang(lang);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode sigNameList = objectMapper.readTree(querySigName(lang));
            if (sigNameList.get("data") == null || sigNameList.get("data").get("SIG_list") == null) {
                throw new IllegalArgumentException("Invalid sig parameter");
            }
            String urlStr = "";
            for (JsonNode bucket : sigNameList.get("data").get("SIG_list")) {
                if (bucket.get("name").asText().equalsIgnoreCase(sig)) {
                    urlStr = bucket.get("links").asText().replace("/blob/", "/raw/").replace("/tree/", "/raw/");
                }
            }
            return httpRequest(urlStr, true);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    /**
     * jump request.
     *
     * @param lang          语言 .
     * @param ecosystemType ecosystemType .
     * @param page          page .
     * @return String.
     */
    public String getEcosystemRepoInfo(String ecosystemType, String page, String lang) {
        String community = ThreadLocalCache.getDataSource();
        String urlStr = String.format(Locale.ROOT, repoInfoApi, community, ecosystemType, page, lang);
        String res = null;
        try {
            res = httpRequest(urlStr, false);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return res;
    }

    /**
     * jump request.
     *
     * @param community 社区 .
     * @param body      body .
     * @return String.
     */
    public String getNps(String community, NpsConditon body) {
        community = ParameterUtil.vaildCommunity(community);
        String urlStr = String.format(npsApi, community);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String bodyStr = objectMapper.writeValueAsString(body);
            return postRequest(urlStr, bodyStr);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    /**
     * send http  request.
     *
     * @param urlStr urlStr .
     * @param flag   flag .
     * @return httpresponce.
     */
    public String httpRequest(String urlStr, Boolean flag) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int timeout = 15000; // 设置超时时间为15秒
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                StandardCharsets.UTF_8))) {
            String line;
            StringJoiner response = new StringJoiner(flag ? "\n" : "");
            while ((line = reader.readLine()) != null) {
                response.add(line);
            }
            return response.toString();
        } finally {
            connection.disconnect(); // 断开连接
        }
    }

    /**
     * send post Request.
     *
     * @param urlStr urlStr .
     * @param body   body .
     * @return httpresponce.
     */
    public String postRequest(String urlStr, String body) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        int timeout = 15000; // 设置超时时间为15秒
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        try (OutputStream outputStream = connection.getOutputStream();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
            writer.write(body);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } finally {
            connection.disconnect(); // 断开连接
        }
    }
}
