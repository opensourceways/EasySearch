package com.search.EaseSearchsearch.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.search.EaseSearchsearch.service.DsApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class DsApiServiceImpl implements DsApiService {

    @Value("${api.mySystem}")
    private String mySystem;

    @Value("${api.allApi}")
    private String allApi;

    @Value("${api.sigNameApi}")
    private String sigNameApi;

    @Value("${api.sigReadmeApi}")
    private String sigReadmeApi;

    @Value("${api.repoInfoApi}")
    private String repoInfoApi;


    @Override
    public String querySigName(String lang) throws Exception {
        String community = mySystem.toLowerCase(Locale.ROOT);
        String urlStr = String.format(Locale.ROOT, sigNameApi, community, lang);
        return httpRequest(urlStr);
    }

    @Override
    public String queryAll() throws Exception {
        String community = mySystem.toLowerCase(Locale.ROOT);
        String urlStr = String.format(Locale.ROOT, allApi, community);
        return httpRequest(urlStr);
    }

    @Override
    public String querySigReadme(String sig, String lang) throws Exception {
        String community = mySystem.toLowerCase(Locale.ROOT);
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> sigName = new ArrayList<>();
        JsonNode sigNameList = objectMapper.readTree(querySigName(lang));
        if (sigNameList.get("data") != null && sigNameList.get("data").get("SIG_list") != null) {
            for (JsonNode bucket : sigNameList.get("data").get("SIG_list")) {
                sigName.add(bucket.get("name").asText());
            }
        }
        if (!sigName.contains(sig)) {
            throw new IllegalArgumentException("Invalid sig parameter");
        }
        sig = sig.replaceAll("\\+", "%20").replaceAll(" ", "%20");
        String urlStr = String.format(Locale.ROOT, sigReadmeApi, community, sig, lang);
        return httpRequest(urlStr);
    }

    @Override
    public String getEcosystemRepoInfo(String ecosystemType, String sortType, String sortOrder, String page, String pageSize, String lang) throws Exception {
        String community = mySystem.toLowerCase(Locale.ROOT);
        String urlStr = String.format(Locale.ROOT, repoInfoApi, community, ecosystemType, sortType, sortOrder, page, pageSize, lang);
        return httpRequest(urlStr);
    }

    public String httpRequest(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }
}
