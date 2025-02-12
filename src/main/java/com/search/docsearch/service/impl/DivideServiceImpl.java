package com.search.docsearch.service.impl;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.search.docsearch.entity.vo.SearchTags;
import com.search.docsearch.service.SearchService;
import com.search.docsearch.utils.General;
import lombok.SneakyThrows;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.search.docsearch.config.MySystem;
import com.search.docsearch.entity.vo.SearchDocs;
import com.search.docsearch.except.ServiceImplException;
import com.search.docsearch.service.DivideService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DivideServiceImpl implements DivideService {

    @Autowired
    @Qualifier("elasticsearchClient")
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    @Qualifier("setConfig")
    private MySystem s;

    @Autowired
    SearchService searchService;

    @SneakyThrows
    @Override
    public Map<String, Object> advancedSearch(Map<String, String> search, String category) throws ServiceImplException {
        String saveIndex;
        String lang = search.get("lang");
        if (lang != null) {
            saveIndex = s.index + "_" + lang;
        } else {
            //在没有传语言时默认为zh
            saveIndex = s.index + "_zh";
        }
        SearchRequest request = new SearchRequest(saveIndex);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.termQuery("type" + ".keyword", category));


        int page = 1;
        int pageSize = 10;
        String keyword = "";
        String path = "";
        String version = "";
        for (Map.Entry<String, String> entry : search.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "page":
                    page = Integer.parseInt(entry.getValue());
                    continue;
                case "pageSize":
                    pageSize = Integer.parseInt(entry.getValue());
                    continue;
                case "keyword":
                    keyword = entry.getValue();
                    continue;
                case "path":
                    path = entry.getValue();
                    continue;
                case "version":
                    version = entry.getValue();
            }

            boolQueryBuilder.filter(QueryBuilders.termQuery(key + ".keyword", value));
        }
        if ("docs".equals(category) && StringUtils.hasText(path) && StringUtils.hasText(version)) {
            StringBuilder pathBuilder = new StringBuilder();
            pathBuilder.append(lang).append("/docs/").append(version).append("/docs/").append(path).append("*");

            WildcardQueryBuilder pathWildcard = QueryBuilders.wildcardQuery("path.keyword",
                    pathBuilder.toString());
            pathWildcard.boost(3);
            boolQueryBuilder.should(pathWildcard);
        }
        int startIndex = (page - 1) * pageSize;

        if (StringUtils.hasText(keyword)) {
            MatchQueryBuilder titleMP = QueryBuilders.matchQuery("title", keyword);
            titleMP.boost(2);
            MatchQueryBuilder textContentMP = QueryBuilders.matchQuery("textContent", keyword);
            textContentMP.boost(1);
            boolQueryBuilder.should(titleMP).should(textContentMP);

            boolQueryBuilder.minimumShouldMatch(1);
        }

        sourceBuilder.from(startIndex).size(pageSize);
        sourceBuilder.query(boolQueryBuilder);

        if ("showcase".equals(category)) {
            if (search.containsKey("industry")) {
                sourceBuilder.sort("typeOrder", SortOrder.DESC);
            } else {
                sourceBuilder.sort("allOrder", SortOrder.DESC);
            }
        } else {
            sourceBuilder.sort("date", SortOrder.DESC);
        }

        request.source(sourceBuilder);

        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
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

    @SneakyThrows
    @Override
    public Map<String, Object> docsSearch(SearchDocs searchDocs) throws ServiceImplException {
        String saveIndex = s.index + "_" + searchDocs.getLang();

        Map<String, Object> result = new HashMap<>();

        int startIndex = (searchDocs.getPage() - 1) * searchDocs.getPageSize();
        SearchRequest request = new SearchRequest(saveIndex);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.filter(QueryBuilders.termQuery("type.keyword", "docs"));

        if (StringUtils.hasText(searchDocs.getVersion())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("version.keyword", searchDocs.getVersion()));
        }
        searchDocs.setKeyword(General.replacementCharacter(searchDocs.getKeyword()));

        MatchPhraseQueryBuilder ptitleMP = QueryBuilders.matchPhraseQuery("title", searchDocs.getKeyword()).analyzer("ik_max_word").slop(2);
        ptitleMP.boost(200);
        MatchPhraseQueryBuilder ptextContentMP = QueryBuilders.matchPhraseQuery("textContent", searchDocs.getKeyword()).analyzer("ik_max_word").slop(2);
        ptextContentMP.boost(100);

        boolQueryBuilder.should(ptitleMP).should(ptextContentMP);

        MatchQueryBuilder titleMP = QueryBuilders.matchQuery("title", searchDocs.getKeyword());
        titleMP.boost(2);
        MatchQueryBuilder textContentMP = QueryBuilders.matchQuery("textContent", searchDocs.getKeyword());
        textContentMP.boost(1);
        boolQueryBuilder.should(titleMP).should(textContentMP);

        boolQueryBuilder.minimumShouldMatch(1);

        sourceBuilder.query(boolQueryBuilder);

        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field("textContent")
                .field("title")
                .fragmentSize(100)
                .preTags("<span>")
                .postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
        sourceBuilder.from(startIndex).size(searchDocs.getPageSize());
        sourceBuilder.timeout(TimeValue.timeValueMinutes(1L));
        request.source(sourceBuilder);
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ServiceImplException("can not search");
        }

        List<Map<String, Object>> data = new ArrayList<>();

        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();
            map.put("score", hit.getScore());
            String text = (String) map.getOrDefault("textContent", "");
            if (text.length() > 200) {
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

        if (s.index.contains("opengauss") && data.size() > 2) {
            SearchTags searchTags = new SearchTags();
            searchTags.setCategory("docs");
            searchTags.setLang(searchDocs.getLang());
            searchTags.setWant("version");
            Map<String, Object> tags = searchService.getTags(searchTags);
            ArrayList<String> versionList = new ArrayList<>();
            if (tags != null) {
                List totalNumList = (List) tags.get("totalNum");
                for (Object o : totalNumList) {
                    Map o1 = (Map) o;
                    versionList.add(o1.get("key") + "");
                }
            }
            ArrayList<Map> scoreList = new ArrayList<>();

            Map<String, List<Map<String, Object>>> articleName = data.stream().collect(Collectors.groupingBy(m -> {
                return String.valueOf(m.get("articleName"));
            }));
            articleName.forEach((k, v) -> {
                v.sort((u1, u2) -> Float.compare(Float.parseFloat(String.valueOf(u2.get("score"))), Float.parseFloat(String.valueOf(u1.get("score")))));
                scoreList.add(v.get(0));
                v.sort((u1, u2) -> Integer.compare(versionList.indexOf(String.valueOf(u1.get("version"))), versionList.indexOf(String.valueOf(u2.get("version")))));
            });
            scoreList.sort((u1, u2) -> Float.compare(Float.parseFloat(String.valueOf(u2.get("score"))), Float.parseFloat(String.valueOf(u1.get("score")))));
            data.clear();
            scoreList.stream().forEach(a -> {
                data.addAll(articleName.get(a.get("articleName")));
            });
        }
        if (data.isEmpty()) {
            return null;
        }

        result.put("page", searchDocs.getPage());
        result.put("pageSize", searchDocs.getPageSize());
        result.put("count", response.getHits().getTotalHits().value);
        result.put("records", data);
        return result;
    }

}
