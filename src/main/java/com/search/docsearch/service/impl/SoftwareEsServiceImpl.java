package com.search.docsearch.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.search.docsearch.config.SoftwareSearchConfig;
import com.search.docsearch.dto.software.*;
import com.search.docsearch.entity.software.SoftwareSearchCondition;
import com.search.docsearch.entity.software.SoftwareSearchResponce;
import com.search.docsearch.entity.software.SoftwareSearchTags;
import com.search.docsearch.enums.SoftwareTypeEnum;
import com.search.docsearch.except.ServiceException;
import com.search.docsearch.except.ServiceImplException;
import com.search.docsearch.service.ISoftwareEsSearchService;
import com.search.docsearch.utils.General;
import com.search.docsearch.utils.JacksonUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class SoftwareEsServiceImpl implements ISoftwareEsSearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    SoftwareSearchConfig searchConfig;

    @Override
    public SoftwareSearchResponce searchByCondition(SoftwareSearchCondition condition) throws ServiceException {

        SearchRequest request = buildSearchRequest(condition, searchConfig.getIndex());

        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);

        } catch (IOException e) {
            throw new ServiceImplException("can not search");
        }
        if (response != null) {
            return handEsResponce(response);
        }

        return null;
    }

    @Override
    public Map<String, Object> getCount(SoftwareSearchCondition condition) throws ServiceException {
        return null;
    }

    @Override
    public List<SearchTagsDto>  getTags(SoftwareSearchTags searchTags) throws ServiceException {
        List<SearchTagsDto> numberList = new ArrayList<>();
        SearchRequest request = new SearchRequest(searchConfig.getIndex());
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.hasText(searchTags.getDataType())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("dataType.keyword", searchTags.getDataType()));
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
        } catch (IOException e) {
            throw new ServiceImplException("can not search");
        }
        ParsedTerms aggregation = response.getAggregations().get("data");

        List<? extends Terms.Bucket> buckets = aggregation.getBuckets();
        for (Terms.Bucket bucket : buckets) {


            SearchTagsDto searchTagsDto = new SearchTagsDto();
            searchTagsDto.setCount(bucket.getDocCount());
            searchTagsDto.setKey( bucket.getKeyAsString());
            numberList.add(searchTagsDto);
        }
        return numberList;
    }


    private SoftwareSearchResponce handEsResponce(SearchResponse response) {
        List<Map<String, Object>> data = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();
            String description = (String) map.getOrDefault("description", "");
            if (null != description && description.length() > 200) {
                description = description.substring(0, 200) + "......";
            }
            map.put("description", description);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields.containsKey("description")) {
                StringBuilder descriptionHighlight = new StringBuilder();
                for (Text textContent : highlightFields.get("description").getFragments()) {
                    descriptionHighlight.append(textContent.toString()).append("<br>");
                }
                map.put("description", descriptionHighlight.toString());
            }
            if (highlightFields.containsKey("name")) {
                map.put("name", highlightFields.get("name").getFragments()[0].toString());
            }
            if (highlightFields.containsKey("summary")) {
                map.put("summary", highlightFields.get("summary").getFragments()[0].toString());
            }

            data.add(map);
        }
        SoftwareSearchResponce searchResponce = getSearchResponce(data);
        searchResponce.setTotal(response.getHits().getTotalHits().value);
        return searchResponce;
    }


    private SoftwareSearchResponce getSearchResponce(List<Map<String, Object>> data) {
        SoftwareSearchResponce searchResponce = new SoftwareSearchResponce();
        Map<String, List<Map<String, Object>>> dataTypeMap = data.stream().collect(Collectors.groupingBy(m -> {
            return String.valueOf(m.get("dataType"));
        }));

        for (SoftwareTypeEnum value : SoftwareTypeEnum.values()) {
            List<Map<String, Object>> maps = dataTypeMap.get(value.getType());
            if (CollectionUtils.isEmpty(maps))
                continue;
            switch (value) {
                case APPLICATION:
                    searchResponce.setAll(convertAppMapToSoftwareAppDto(maps, "all"));
                    searchResponce.setAppkg(convertAppMapToSoftwareAppDto(maps, null));
                    break;
                case RPMPKG:
                    searchResponce.setRpmpkg(convertAppMapToSoftwareRpmDto(maps));
                    break;

                case EKPG:
                    searchResponce.setEpkgpkg(convertAppMapToSoftwareEpkgDto(maps));
                    break;
            }
        }


        return searchResponce;
    }


    private List<SoftwareAppDto> convertAppMapToSoftwareAppDto(List<Map<String, Object>> maps, String type) {
        List<SoftwareAppDto> softwareAppDtoList = new ArrayList<>();
        Map<String, List<Map<String, Object>>> categoryMap = maps.stream().collect(Collectors.groupingBy(m -> {
            return String.valueOf(m.get("category"));
        }));
        for (Map.Entry<String, List<Map<String, Object>>> stringListEntry : categoryMap.entrySet()) {
            SoftwareAppDto softwareAppDto = new SoftwareAppDto(stringListEntry.getKey());
            for (Map<String, Object> stringObjectMap : stringListEntry.getValue()) {
                SoftwareAppChildrenDto softwareAppChildrenDto = JacksonUtils.toObject(SoftwareAppChildrenDto.class, JSONObject.toJSONString(stringObjectMap));
                String tagsText = String.valueOf(stringObjectMap.get("tagsText"));
                if (tagsText != null && "all".equals(type)) {
                    tagsText = tagsText.toUpperCase(Locale.ROOT);
                    softwareAppChildrenDto.setTags(Arrays.asList(tagsText.split(",")));
                } else {
                    softwareAppChildrenDto.setTags(Arrays.asList("IMAGE"));
                }

                softwareAppDto.getChildren().add(softwareAppChildrenDto);
            }
            softwareAppDtoList.add(softwareAppDto);
        }
        return softwareAppDtoList;

    }


    private <T> List<T> convertAppMapToSoftwareDto(List<Map<String, Object>> maps, Class<T> clazz) {
        List<T> softwareAppDtoList = new ArrayList<>();

        maps.stream().forEach(m -> {
            softwareAppDtoList.add(JacksonUtils.toObject(clazz, JSONObject.toJSONString(m)));
        });
        return softwareAppDtoList;
    }

    private List<SoftwareEpkgDto> convertAppMapToSoftwareEpkgDto(List<Map<String, Object>> maps) {
        List<SoftwareEpkgDto> softwareEpkgDtoList = new ArrayList<>();

        maps.stream().forEach(m -> {
            m.put("epkgUpdateAt", m.get("updatetime"));
            m.put("epkgCategory", m.get("category"));
            m.put("epkgSize", m.get("size"));
            softwareEpkgDtoList.add(JacksonUtils.toObject(SoftwareEpkgDto.class, JSONObject.toJSONString(m)));
        });
        return softwareEpkgDtoList;
    }

    private List<SoftwareRpmDto> convertAppMapToSoftwareRpmDto(List<Map<String, Object>> maps) {
        List<SoftwareRpmDto> softwareAppDtoList = new ArrayList<>();

        maps.stream().forEach(m -> {
            m.put("rpmUpdateAt", m.get("updatetime"));
            m.put("rpmCategory", m.get("category"));
            m.put("rpmSize", m.get("size"));
            softwareAppDtoList.add(JacksonUtils.toObject(SoftwareRpmDto.class, JSONObject.toJSONString(m)));
        });
        return softwareAppDtoList;
    }


    private SearchRequest buildSearchRequest(SoftwareSearchCondition condition, String index) {
        int startIndex = (condition.getPageNum() - 1) * condition.getPageSize();
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        String typeByfrontDeskType = SoftwareTypeEnum.getTypeByfrontDeskType(condition.getDataType());
        if (StringUtils.hasText(typeByfrontDeskType)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("dataType.keyword", typeByfrontDeskType));
        }

        condition.setKeyword(General.replacementCharacter(condition.getKeyword()));

        MatchPhraseQueryBuilder titleMP = QueryBuilders.matchPhraseQuery("name", condition.getKeyword()).analyzer("ik_max_word").slop(2);
        titleMP.boost(1000);
        MatchPhraseQueryBuilder descriptionBuilder = QueryBuilders.matchPhraseQuery("description", condition.getKeyword()).analyzer("ik_max_word");
        descriptionBuilder.boost(500);
        MatchPhraseQueryBuilder summaryBuilder = QueryBuilders.matchPhraseQuery("summary", condition.getKeyword()).analyzer("ik_max_word");
        summaryBuilder.boost(500);
        ;
        boolQueryBuilder.should(titleMP).should(descriptionBuilder).should(summaryBuilder);
        boolQueryBuilder.minimumShouldMatch(1);

        if (condition.getArch() != null) {
            BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
            vBuilder.mustNot(QueryBuilders.termsQuery("arch.keyword", condition.getArch()));
            boolQueryBuilder.mustNot(vBuilder);
        }


        if (condition.getCategory() != null) {
            BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
            vBuilder.mustNot(QueryBuilders.termsQuery("category.keyword", condition.getCategory()));
            boolQueryBuilder.mustNot(vBuilder);
        }


        if (condition.getVersion() != null) {
            BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
            vBuilder.mustNot(QueryBuilders.termsQuery("version.keyword", condition.getVersion()));
            boolQueryBuilder.mustNot(vBuilder);
        }

        if (condition.getOs() != null) {
            BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
            vBuilder.mustNot(QueryBuilders.termsQuery("os.keyword", condition.getOs()));
            boolQueryBuilder.mustNot(vBuilder);
        }

        sourceBuilder.query(boolQueryBuilder);
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field("description")
                .field("name")
                .field("summary")
                .fragmentSize(100)
                .preTags("<span>")
                .postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
        sourceBuilder.from(startIndex).size(condition.getPageSize());
        sourceBuilder.timeout(TimeValue.timeValueMinutes(1L));
        request.source(sourceBuilder);
        return request;
    }
}