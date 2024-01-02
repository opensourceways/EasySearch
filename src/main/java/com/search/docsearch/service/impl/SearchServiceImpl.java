package com.search.docsearch.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.term.TermSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.search.docsearch.config.MySystem;
import com.search.docsearch.entity.vo.NpsBody;
import com.search.docsearch.entity.vo.SearchCondition;
import com.search.docsearch.entity.vo.SearchTags;
import com.search.docsearch.except.ServiceImplException;
import com.search.docsearch.service.SearchService;
import com.search.docsearch.utils.General;
import com.search.docsearch.utils.ParameterUtil;


@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    @Qualifier("elasticsearchClient")
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    @Qualifier("setConfig")
    private MySystem mySystem;

    @Value("${api.allApi}")
    private String allApi;

    @Value("${api.starsApi}")
    private String starsApi;

    @Value("${api.sigNameApi}")
    private String sigNameApi;

    @Value("${api.repoInfoApi}")
    private String repoInfoApi;

    @Value("${api.npsApi}")
    private String npsApi;

    public Map<String, Object> getSuggestion(String keyword, String lang) throws ServiceImplException {
        String saveIndex = mySystem.index + "_" + lang;

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SuggestionBuilder<TermSuggestionBuilder> termSuggestionBuilder =
                SuggestBuilders.termSuggestion("textContent")
                        .text(keyword)
                        .minWordLength(2)
                        .prefixLength(0)
                        .analyzer("ik_smart")
                        .size(3)
                        .suggestMode(TermSuggestionBuilder.SuggestMode.ALWAYS);

        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("my_sugg", termSuggestionBuilder);

        SearchRequest suggRequest = new SearchRequest(saveIndex);

        suggRequest.source(searchSourceBuilder.suggest(suggestBuilder));


        SearchResponse suggResponse = null;
        try {
            suggResponse = restHighLevelClient.search(suggRequest, RequestOptions.DEFAULT);
        } catch(IOException e) {
            throw new ServiceImplException("can not search");
        }

        List<String> suggestList = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            StringBuilder sb = new StringBuilder();
            boolean isNew = false;
            for (Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option> my_sugg : suggResponse.getSuggest().getSuggestion("my_sugg")) {

                String op = my_sugg.getText().string();

                boolean hc = General.haveChinese(op);

                if (!hc) {
                    if (my_sugg.getOptions().size() > i) {
                        op = my_sugg.getOptions().get(i).getText().string();
                        isNew = true;
                        sb.append("<em>").append(op).append("</em>").append(" ");
                    } else {
                        sb.append(op).append(" ");
                    }
                } else {
                    sb.append(op);
                }
            }


            if (isNew) {
                suggestList.add(sb.toString().trim());
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("suggestList", suggestList);
        return result;


    }


    @Override
    public Map<String, Object> searchByCondition(SearchCondition condition) throws ServiceImplException {
        String saveIndex = mySystem.index + "_" + condition.getLang();

        Map<String, Object> result = new HashMap<>();
        result.put("keyword", HtmlUtils.htmlEscape(condition.getKeyword()));

        SearchRequest request = BuildSearchRequest(condition, saveIndex);
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch(IOException e) {
            throw new ServiceImplException("can not search");
        }
        List<Map<String, Object>> data = new ArrayList<>();

        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();
            String text = (String)map.getOrDefault("textContent", "");
            if (null != text && text.length() > 200) {
                text = text.substring(0, 200) + "......";
            }
            map.put("textContent", text);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields.containsKey("textContent")) {
                StringBuilder highLight = new StringBuilder();
                for (Text textContent : highlightFields.get("textContent").getFragments()) {
                    highLight.append(textContent.toString()).append("<br>");
                }
                map.put("textContent", highLight.toString());
            }

            if (highlightFields.containsKey("title")) {
                map.put("title", highlightFields.get("title").getFragments()[0].toString());
            }

            data.add(map);
        }
        if (data.isEmpty()) {
            return null;
        }


        result.put("page", condition.getPage());
        result.put("pageSize", condition.getPageSize());
        result.put("records", data);
        return result;
    }

    public SearchRequest BuildSearchRequest(SearchCondition condition, String index) {
        int startIndex = (condition.getPage() - 1) * condition.getPageSize();
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (StringUtils.hasText(condition.getType())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("type.keyword", condition.getType()));
        }
        //因为会出现一些特殊的字符导致分词出错（比如英文连接词），在这里处理一下
        condition.setKeyword(General.replacementCharacter(condition.getKeyword()));

        MatchPhraseQueryBuilder ptitleMP = QueryBuilders.matchPhraseQuery("title", condition.getKeyword()).analyzer("ik_max_word").slop(2);
        ptitleMP.boost(200);
        MatchPhraseQueryBuilder ptextContentMP = QueryBuilders.matchPhraseQuery("textContent", condition.getKeyword()).analyzer("ik_max_word").slop(2);
        ptextContentMP.boost(100);

        boolQueryBuilder.should(ptitleMP).should(ptextContentMP);

        MatchQueryBuilder titleMP = QueryBuilders.matchQuery("title", condition.getKeyword()).analyzer("ik_smart");
        titleMP.boost(2);
        MatchQueryBuilder textContentMP = QueryBuilders.matchQuery("textContent", condition.getKeyword()).analyzer("ik_smart");
        textContentMP.boost(1);
        boolQueryBuilder.should(titleMP).should(textContentMP);

        boolQueryBuilder.minimumShouldMatch(1);

        if (condition.getLimit() != null) {
            for (Map<String, String> map : condition.getLimit()) {
                BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("version")) {
                        String[] versions = value.split(",");
                        vBuilder.mustNot(QueryBuilders.termsQuery("version.keyword", versions));
                    } else {
                        vBuilder.must(QueryBuilders.termQuery(key + ".keyword", value));
                    }
                }
                boolQueryBuilder.mustNot(vBuilder);
            }
        }

        if (condition.getFilter() != null) {
            BoolQueryBuilder zBuilder = QueryBuilders.boolQuery();
            for (Map<String, String> map : condition.getFilter()) {
                BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("version")) {
                        String[] versions = value.split(",");
                        vBuilder.must(QueryBuilders.termsQuery("version.keyword", versions));
                    } else {
                        vBuilder.must(QueryBuilders.termQuery(key + ".keyword", value));
                    }
                }
                zBuilder.should(vBuilder);
            }
            boolQueryBuilder.filter(zBuilder);
        }

        sourceBuilder.query(boolQueryBuilder);
        if(condition.getType() ==null || "".equals(condition.getType().trim())){
            sourceBuilder.sort("type.keyword", SortOrder.ASC);
        }
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field("textContent")
                .field("title")
                .fragmentSize(100)
                .preTags("<span>")
                .postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
        sourceBuilder.from(startIndex).size(condition.getPageSize());
        sourceBuilder.timeout(TimeValue.timeValueMinutes(1L));
        request.source(sourceBuilder);
        return request;
    }


    public Map<String, Object> getCount(SearchCondition condition) throws ServiceImplException {
        String saveIndex = mySystem.index + "_" + condition.getLang();
        SearchRequest request = new SearchRequest(saveIndex);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //因为会出现一些特殊的字符导致分词出错（比如英文连接词），在这里处理一下
        condition.setKeyword(General.replacementCharacter(condition.getKeyword()));

        MatchPhraseQueryBuilder ptitleMP = QueryBuilders.matchPhraseQuery("title", condition.getKeyword()).analyzer("ik_max_word").slop(2);
        MatchPhraseQueryBuilder ptextContentMP = QueryBuilders.matchPhraseQuery("textContent", condition.getKeyword()).analyzer("ik_max_word").slop(2);

        boolQueryBuilder.should(ptitleMP).should(ptextContentMP);

        MatchQueryBuilder titleMP = QueryBuilders.matchQuery("title", condition.getKeyword()).analyzer("ik_smart");
        MatchQueryBuilder textContentMP = QueryBuilders.matchQuery("textContent", condition.getKeyword()).analyzer("ik_smart");
        boolQueryBuilder.should(titleMP).should(textContentMP);

        boolQueryBuilder.minimumShouldMatch(1);

        if (condition.getLimit() != null) {
            for (Map<String, String> map : condition.getLimit()) {
                BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("version")) {
                        String[] versions = value.split(",");
                        vBuilder.mustNot(QueryBuilders.termsQuery("version.keyword", versions));
                    } else {
                        vBuilder.must(QueryBuilders.termQuery(key + ".keyword", value));
                    }
                }
                boolQueryBuilder.mustNot(vBuilder);
            }
        }

        if (condition.getFilter() != null) {
            BoolQueryBuilder zBuilder = QueryBuilders.boolQuery();
            for (Map<String, String> map : condition.getFilter()) {
                BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("version")) {
                        String[] versions = value.split(",");
                        vBuilder.must(QueryBuilders.termsQuery("version.keyword", versions));
                    } else {
                        vBuilder.must(QueryBuilders.termQuery(key + ".keyword", value));
                    }
                }
                zBuilder.should(vBuilder);
            }
            boolQueryBuilder.filter(zBuilder);
        }

        sourceBuilder.query(boolQueryBuilder);

        sourceBuilder.aggregation(AggregationBuilders.terms("data").field("type.keyword"));
        request.source(sourceBuilder);
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch(IOException e) {
            throw new ServiceImplException("can not search");
        }
        List<Map<String, Object>> numberList = new ArrayList<>();
        Map<String, Object> numberMap = new HashMap<>();
        numberMap.put("doc_count", response.getHits().getTotalHits().value);
        numberMap.put("key", "all");
        numberList.add(numberMap);
        ParsedTerms aggregation = response.getAggregations().get("data");
        List<? extends Terms.Bucket> buckets = aggregation.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            Map<String, Object> countMap = new HashMap<>();
            countMap.put("key", bucket.getKeyAsString());
            countMap.put("doc_count", bucket.getDocCount());
            numberList.add(countMap);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("total", numberList);
        return result;
    }

    @Override
    public Map<String, Object> advancedSearch(Map<String, String> search) throws ServiceImplException {
        String saveIndex;
        String lang = search.get("lang");
        if (lang != null) {
            saveIndex = mySystem.index + "_" + lang;
        } else {
            //在没有传语言时默认为zh
            saveIndex = mySystem.index + "_zh";
        }
        SearchRequest request = new SearchRequest(saveIndex);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        int page = 1;
        int pageSize = 10;
        String keyword = "";
        for (Map.Entry<String, String> entry : search.entrySet()) {
            if (entry.getKey().equals("page")) {
                page = Integer.parseInt(entry.getValue());
                continue;
            }
            if (entry.getKey().equals("pageSize")) {
                pageSize = Integer.parseInt(entry.getValue());
                continue;
            }
            if (entry.getKey().equals("keyword")) {
                keyword = entry.getValue();
                continue;
            }

            boolQueryBuilder.filter(QueryBuilders.termQuery(entry.getKey() + ".keyword", entry.getValue()));
        }

        int startIndex = (page - 1) * pageSize;

        if (!keyword.equals("")) {
            MatchQueryBuilder titleMP = QueryBuilders.matchQuery("title", keyword);
            titleMP.boost(2);
            MatchQueryBuilder textContentMP = QueryBuilders.matchQuery("textContent", keyword);
            textContentMP.boost(1);
            boolQueryBuilder.should(titleMP).should(textContentMP);

            boolQueryBuilder.minimumShouldMatch(1);

            HighlightBuilder highlightBuilder = new HighlightBuilder()
                    .field("textContent")
                    .field("title")
                    .fragmentSize(100)
                    .preTags("<span>")
                    .postTags("</span>");
            sourceBuilder.highlighter(highlightBuilder);
        }
        sourceBuilder.from(startIndex).size(pageSize);

        sourceBuilder.query(boolQueryBuilder);

        sourceBuilder.sort("date", SortOrder.DESC);

        request.source(sourceBuilder);

        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch(IOException e) {
            throw new ServiceImplException("can not search");
        }
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();

            data.add(map);
        }
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("count", response.getHits().getTotalHits().value);
        result.put("records", data);
        return result;
    }

    @Override
    public Map<String, Object> getTags(SearchTags searchTags) throws ServiceImplException {
        String saveIndex = mySystem.index + "_" + searchTags.getLang();

        SearchRequest request = new SearchRequest(saveIndex);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.hasText(searchTags.getCategory())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("type.keyword", searchTags.getCategory()));
        }

        if (searchTags.getCondition() != null) {
            for (Map.Entry<String, String> entry : searchTags.getCondition().entrySet()) {
                boolQueryBuilder.filter(QueryBuilders.termQuery(entry.getKey() + ".keyword", entry.getValue()));
            }
        }

        BucketOrder bucketOrder = BucketOrder.key(false);
        sourceBuilder.aggregation(AggregationBuilders.terms("data").field(searchTags.getWant() + ".keyword").size(10000).order(bucketOrder));
        sourceBuilder.query(boolQueryBuilder);
        request.source(sourceBuilder);
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch(IOException e) {
            throw new ServiceImplException("can not search");
        }
        ParsedTerms aggregation = response.getAggregations().get("data");
        List<Map<String, Object>> numberList = new ArrayList<>();
        List<? extends Terms.Bucket> buckets = aggregation.getBuckets();
        for (Terms.Bucket bucket : buckets) {

            Map<String, Object> countMap = new HashMap<>();

            countMap.put("key", bucket.getKeyAsString());
            countMap.put("count", bucket.getDocCount());
            numberList.add(countMap);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("totalNum", numberList);
        return result;
    }

    @Override
    public String querySigName(String lang) throws ServiceImplException {
        String community = mySystem.getSystem();
        String urlStr = String.format(Locale.ROOT, sigNameApi, community, lang);  
        String res = null;
        try {
            res = httpRequest(urlStr, false);
        } catch(IOException e) {
            throw new ServiceImplException("can not search");
        }
        return res;
    }

    @Override
    public String queryAll() throws ServiceImplException {
        String community = mySystem.getSystem();
        String urlStr = String.format(Locale.ROOT, allApi, community);  
        String res = null;
        try {
            res = httpRequest(urlStr, false);
        } catch(IOException e) {
            throw new ServiceImplException("can not search");
        }
        return res;
    }

    @Override
    public String queryStars() throws ServiceImplException {
        String community = mySystem.getSystem();
        String urlStr = String.format(Locale.ROOT, starsApi, community);  
        String res = null;
        try {
            res = httpRequest(urlStr, false);
        } catch(IOException e) {
            throw new ServiceImplException("can not search");
        }
        return res;
    }

    @Override
    public String querySigReadme(String sig, String lang) throws ServiceImplException {
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
            throw new ServiceImplException("can not search");
        }
    }

    @Override
    public String getEcosystemRepoInfo(String ecosystemType, String page, String lang) throws ServiceImplException {     
        String community = mySystem.getSystem();
        String urlStr = String.format(Locale.ROOT, repoInfoApi, community, ecosystemType, page, lang);
        String res = null;
        try {
            res = httpRequest(urlStr, false);
        } catch(IOException e) {
            throw new ServiceImplException("can not search");
        }
        return res;
    }


    @Override
    public String getNps(String community, NpsBody body) throws ServiceImplException {     
        community = ParameterUtil.vaildCommunity(community);
        String urlStr = String.format(npsApi, community);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String bodyStr = objectMapper.writeValueAsString(body);
            return postRequest(urlStr, bodyStr); 
        } catch (JsonProcessingException e) {
            throw new ServiceImplException("can not process json");
        } catch (Exception e) {
            throw new ServiceImplException("can not post data");
        }
    }


    public String httpRequest(String urlStr, Boolean flag) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
            if (flag) {
                response.append("\n");
            }
        }
        reader.close();
        return response.toString();
    }

    public String postRequest(String urlStr, String body) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(body.getBytes());
        outputStream.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }
}
