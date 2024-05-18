package com.search.docsearch.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.search.docsearch.config.SoftwareSearchConfig;
import com.search.docsearch.constant.Constants;
import com.search.docsearch.dto.software.*;
import com.search.docsearch.entity.software.*;
import com.search.docsearch.enums.SoftwareTypeEnum;
import com.search.docsearch.enums.SoftwarekeywordTypeEnum;
import com.search.docsearch.except.ServiceException;
import com.search.docsearch.except.ServiceImplException;
import com.search.docsearch.service.ISoftwareEsSearchService;
import com.search.docsearch.utils.General;
import com.search.docsearch.utils.JacksonUtils;
import com.search.docsearch.utils.Trie;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;


@Service
public class SoftwareEsServiceImpl implements ISoftwareEsSearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    SoftwareSearchConfig searchConfig;
    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

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
    public List<SearchTagsDto> getTags(SoftwareSearchTags searchTags) throws ServiceException {
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
            searchTagsDto.setKey(bucket.getKeyAsString());
            numberList.add(searchTagsDto);
        }
        return numberList;
    }

    @Override
    public SearchFindwordDto findWord(String prefix, String dataType) throws ServiceException {
        List<Trie.KeyCountResult> keyCountResultList = new ArrayList<>();
        if (!StringUtils.isEmpty(dataType)) {
            SoftwareTypeEnum enumByfrontDeskType = SoftwareTypeEnum.getEnumByfrontDeskType(dataType);
            Trie trie = enumByfrontDeskType.getTrie();
            for (int i = 0; i < 3 && i < prefix.length(); i++) {
                keyCountResultList.addAll(trie.searchTopKWithPrefix(prefix.substring(0, prefix.length() - i), 10));
                if (!CollectionUtils.isEmpty(keyCountResultList))
                    break;
            }
        } else {
            keyCountResultList.addAll(Constants.softwareTrie.searchTopKWithPrefix(prefix, 10));
        }
        SearchFindwordDto searchFindwordDto = new SearchFindwordDto(keyCountResultList);
        return searchFindwordDto;
    }

    @Override
    public List<SoftwareSearchCountResponce> getCountByCondition(SoftwareSearchCondition condition) throws ServiceException {
        List<SoftwareSearchCountResponce> countList = new ArrayList<>();
        SearchRequest request = new SearchRequest(searchConfig.getIndex());
        SearchSourceBuilder searchSourceBuilder = buildSearchSourceBuilderByCondition(condition);
        searchSourceBuilder.aggregation(AggregationBuilders.terms("data").field("dataType.keyword").subAggregation(AggregationBuilders.terms("category").field("category.keyword")));
        request.source(searchSourceBuilder);
        SearchResponse response = null;

        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ServiceImplException("can not search");
        }


        ParsedTerms aggregation = response.getAggregations().get("data");
        List<? extends Terms.Bucket> buckets = aggregation.getBuckets();
        // long allTypeCount = 0L;
        HashMap<String, Long> docCountMap = new HashMap<>();
        if (buckets != null)
            for (Terms.Bucket bucket : buckets) {
              /*  if (SoftwareTypeEnum.APPLICATION.getType().equals(bucket.getKeyAsString())) {
                    allTypeCount += bucket.getDocCount();
                } else {
                    ParsedTerms category = bucket.getAggregations().get("category");
                    List<? extends Terms.Bucket> subBuckets = category.getBuckets();
                    for (Terms.Bucket subBucket : subBuckets) {
                        if (!"其他".equals(subBucket.getKeyAsString()))
                            allTypeCount += subBucket.getDocCount();
                    }
                }*/

                docCountMap.put(SoftwareTypeEnum.getFrontDeskTypeByType(bucket.getKeyAsString()), bucket.getDocCount());
                /*SoftwareSearchCountResponce softwareSearchCountResponce = new SoftwareSearchCountResponce();
                softwareSearchCountResponce.setDocCount(bucket.getDocCount());
                softwareSearchCountResponce.setKey(SoftwareTypeEnum.getFrontDeskTypeByType(bucket.getKeyAsString()));
                countList.add(softwareSearchCountResponce);*/
            }

        /*if (allTypeCount > 0) {
            SoftwareSearchCountResponce softwareSearchCountResponce = new SoftwareSearchCountResponce();
            softwareSearchCountResponce.setDocCount(allTypeCount);
            softwareSearchCountResponce.setKey("all");
            countList.add(softwareSearchCountResponce);
        }*/
        for (SoftwareTypeEnum value : SoftwareTypeEnum.values()) {
            SoftwareSearchCountResponce softwareSearchCountResponce = new SoftwareSearchCountResponce();
            softwareSearchCountResponce.setDocCount(docCountMap.get(value.getFrontDeskType()) == null ? 0 : docCountMap.get(value.getFrontDeskType()));
            softwareSearchCountResponce.setKey(value.getFrontDeskType());
            countList.add(softwareSearchCountResponce);
        }

        return countList;
    }

    @Override
    public List<SoftwareDocsAllResponce> searchAllByCondition(SoftwareSearchCondition condition) throws ServiceException {
        List<SoftwareDocsAllResponce> responce = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(SoftwareTypeEnum.values().length - 1);
        for (SoftwareTypeEnum value : SoftwareTypeEnum.values()) {
            if (SoftwareTypeEnum.ALL.equals(value))
                continue;

            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        SoftwareSearchCondition clone = condition.clone();
                        clone.setDataType(value.getFrontDeskType());
                        SoftwareSearchResponce softwareSearchResponce = searchByCondition(clone);
                        if (softwareSearchResponce.getTotal() > 0) {
                            List<SoftwareNameDocsDto> nameList = new ArrayList<>();
                            switch (value) {
                                case APPLICATION:
                                    List<SoftwareAppChildrenDto> apppkg = softwareSearchResponce.getApppkg();
                                    apppkg.stream().forEach(a -> {
                                        nameList.add(new SoftwareNameDocsDto(a.getName(), a.getPkgId(), a.getVersion()));
                                    });
                                    break;

                                case RPMPKG:
                                    List<SoftwareRpmDto> rpmpkg = softwareSearchResponce.getRpmpkg();
                                    rpmpkg.stream().forEach(a -> {
                                        nameList.add(new SoftwareNameDocsDto(a.getName(), a.getPkgId(), a.getVersion()));
                                    });
                                    break;

                                case EKPG:
                                    List<SoftwareEpkgDto> epkgpkg = softwareSearchResponce.getEpkgpkg();
                                    epkgpkg.stream().forEach(a -> {
                                        nameList.add(new SoftwareNameDocsDto(a.getName(), a.getPkgId(), a.getVersion()));
                                    });
                                    break;

                            }
                            SoftwareDocsAllResponce softwareDocsAllResponce = new SoftwareDocsAllResponce(value.getFrontDeskType(), softwareSearchResponce.getTotal(), nameList);
                            responce.add(softwareDocsAllResponce);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                    }
                }

            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (responce.size() > 0) {
            List<String> sortKeyList = Arrays.asList("rpmpkg", "apppkg", "epkgpkg");
            responce.sort((a, b) -> Integer.compare(sortKeyList.indexOf(a.getKey()),sortKeyList.indexOf(b.getKey())));
        }

        return responce;
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
                    searchResponce.setApppkg(convertAppMapToSoftwareDto(maps));
                    break;
                case RPMPKG:
                    searchResponce.setRpmpkg(convertAppMapToSoftwareRpmDto(maps));
                    break;

                case EKPG:
                    searchResponce.setEpkgpkg(convertAppMapToSoftwareEpkgDto(maps));
                    break;

                case ALL:
                    searchResponce.setAll(convertAllMapToSoftwareDto(maps));
                    break;
            }
        }
        // handleAllType(data, searchResponce);
        return searchResponce;
    }


    /*private void handleAllType(List<Map<String, Object>> data, SoftwareSearchResponce searchResponce) {
        data = data.stream().filter(d -> {
            return SoftwareTypeEnum.APPLICATION.getType().equals(String.valueOf(d.get("dataType"))) || (!SoftwareTypeEnum.APPLICATION.getType().equals(String.valueOf(d.get("dataType"))) && !"其他".equals(String.valueOf(d.get("category"))));
        }).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(data))
            searchResponce.setAll(convertAppMapToSoftwareAppDto(data));
    }*/


    private List<SoftwareAllDto> convertAllMapToSoftwareDto(List<Map<String, Object>> maps) {
        List<SoftwareAllDto> softwareAllDtoList = new ArrayList<>();
        maps.stream().forEach(m -> {
                    if (m.containsKey("pkgIds")) {
                        m.put("pkgIds", JSONObject.parseObject(m.get("pkgIds") + ""));

                    }
                    if (m.containsKey("tags")) {
                        m.put("tags", JSONObject.parseArray(m.get("tags") + ""));

                    }
                    SoftwareAllDto softwareAllDto = JacksonUtils.toObject(SoftwareAllDto.class, JSONObject.toJSONString(m));
                    softwareAllDtoList.add(softwareAllDto);
                }

        );
        return softwareAllDtoList;
    }


    private List<SoftwareAppChildrenDto> convertAppMapToSoftwareDto(List<Map<String, Object>> maps) {
        List<SoftwareAppChildrenDto> softwareAppDtoList = new ArrayList<>();
        maps.stream().forEach(m -> {
                    SoftwareAppChildrenDto softwareAppChildrenDto = JacksonUtils.toObject(SoftwareAppChildrenDto.class, JSONObject.toJSONString(m));
                    if (m.get("tagsText") != null) {
                        String tagsText = String.valueOf(m.get("tagsText"));
                        softwareAppChildrenDto.setTags(Arrays.asList(tagsText.split(",")));
                    }
                    SoftwarePkgIdsDto softwarePkgIdsDto = new SoftwarePkgIdsDto();
                    softwarePkgIdsDto.setEPKG(m.get("EPKG") == null ? "" : String.valueOf(m.get("EPKG")));
                    softwarePkgIdsDto.setIMAGE(m.get("IMAGE") == null ? "" : String.valueOf(m.get("IMAGE")));
                    softwarePkgIdsDto.setRPM(m.get("RPM") == null ? "" : String.valueOf(m.get("RPM")));
                    softwareAppChildrenDto.setPkgIds(softwarePkgIdsDto);
                    softwareAppDtoList.add(softwareAppChildrenDto);
                }

        );
        return softwareAppDtoList;
    }


    private List<SoftwareAppDto> convertAppMapToSoftwareAppDto(List<Map<String, Object>> maps) {
        List<SoftwareAppDto> softwareAppDtoList = new ArrayList<>();
        Map<String, List<Map<String, Object>>> categoryMap = maps.stream().collect(Collectors.groupingBy(m -> {
            return String.valueOf(m.get("category"));
        }));
        for (Map.Entry<String, List<Map<String, Object>>> stringListEntry : categoryMap.entrySet()) {
            SoftwareAppDto softwareAppDto = new SoftwareAppDto(stringListEntry.getKey());
            for (Map<String, Object> stringObjectMap : stringListEntry.getValue()) {
                SoftwareAppChildrenDto softwareAppChildrenDto = JacksonUtils.toObject(SoftwareAppChildrenDto.class, JSONObject.toJSONString(stringObjectMap));
                String tagsText = String.valueOf(stringObjectMap.get("tagsText"));
               /* if (stringObjectMap.get("tagsText") != null && isAll) {
                    tagsText = tagsText.toUpperCase(Locale.ROOT);
                    softwareAppChildrenDto.setTags(Arrays.asList(tagsText.split(",")));
                } else {
                    softwareAppChildrenDto.setTags(Arrays.asList(SoftwareTypeEnum.getTagByDataType(String.valueOf(stringObjectMap.get("dataType")))));
                }*/
                if (stringObjectMap.get("tagsText") != null) {
                    softwareAppChildrenDto.setTags(Arrays.asList(tagsText.split(",")));
                }
                SoftwarePkgIdsDto softwarePkgIdsDto = new SoftwarePkgIdsDto();
                softwarePkgIdsDto.setEPKG(stringObjectMap.get("EPKG") == null ? "" : String.valueOf(stringObjectMap.get("EPKG")));
                softwarePkgIdsDto.setIMAGE(stringObjectMap.get("IMAGE") == null ? "" : String.valueOf(stringObjectMap.get("IMAGE")));
                softwarePkgIdsDto.setRPM(stringObjectMap.get("RPM") == null ? "" : String.valueOf(stringObjectMap.get("RPM")));
                softwareAppChildrenDto.setPkgIds(softwarePkgIdsDto);
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
        SearchSourceBuilder sourceBuilder = buildSearchSourceBuilderByCondition(condition);
        buildHighlightBuilder(sourceBuilder);
        sourceBuilder.from(startIndex).size(condition.getPageSize());
        sourceBuilder.timeout(TimeValue.timeValueMinutes(1L));
        request.source(sourceBuilder);
        return request;
    }


    private void buildHighlightBuilder(SearchSourceBuilder sourceBuilder) {
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field("description")
                .field("name")
                .field("summary")
                .fragmentSize(100)
                .preTags("<span>")
                .postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
    }


    private SearchSourceBuilder buildSearchSourceBuilderByCondition(SoftwareSearchCondition condition) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        String typeByfrontDeskType = SoftwareTypeEnum.getTypeByfrontDeskType(condition.getDataType());
        if (!StringUtils.isEmpty(typeByfrontDeskType)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("dataType.keyword", typeByfrontDeskType));
        }

        condition.setKeyword(General.replacementCharacter(condition.getKeyword()));

        boolean supportKeywordType = SoftwarekeywordTypeEnum.isSupportKeywordType(condition.getKeywordType());
        if (!supportKeywordType || "name".equals(condition.getKeywordType())) {

            MatchPhraseQueryBuilder titleMP = QueryBuilders.matchPhraseQuery("name", condition.getKeyword()).analyzer("ik_max_word").slop(2);
            titleMP.boost(1000);

            WildcardQueryBuilder field = QueryBuilders.wildcardQuery("name", "*" + condition.getKeyword() + "*");
            boolQueryBuilder.should(field);
            boolQueryBuilder.should(titleMP);
           /* MatchQueryBuilder provide = QueryBuilders.matchQuery("providesText", condition.getKeyword());
            provide.boost(1);
            boolQueryBuilder.should(provide);*/
            WildcardQueryBuilder providesWildcard = QueryBuilders.wildcardQuery("providesText", "*" + condition.getKeyword() + "*");
            providesWildcard.boost(1);
            boolQueryBuilder.should(providesWildcard);

           /* MatchQueryBuilder requires = QueryBuilders.matchQuery("requiresText", condition.getKeyword());
            requires.boost(1);
            boolQueryBuilder.should(requires);*/
            WildcardQueryBuilder requiresWildcard = QueryBuilders.wildcardQuery("requiresText", "*" + condition.getKeyword() + "*");
            requiresWildcard.boost(1);
            boolQueryBuilder.should(requiresWildcard);
        }
        if (!supportKeywordType || "description".equals(condition.getKeywordType())) {
            MatchPhraseQueryBuilder descriptionBuilder = QueryBuilders.matchPhraseQuery("description", condition.getKeyword()).analyzer("ik_max_word");
            descriptionBuilder.boost(500);
            boolQueryBuilder.should(descriptionBuilder);
        }

        if (!supportKeywordType || "summary".equals(condition.getKeywordType())) {
            MatchPhraseQueryBuilder summaryBuilder = QueryBuilders.matchPhraseQuery("summary", condition.getKeyword()).analyzer("ik_max_word");
            summaryBuilder.boost(500);
            boolQueryBuilder.should(summaryBuilder);
        }
        boolQueryBuilder.minimumShouldMatch(1);

        if (!StringUtils.isEmpty(condition.getArch())) {
            BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
            vBuilder.mustNot(QueryBuilders.termsQuery("arch.keyword", condition.getArch().split(",")));
            boolQueryBuilder.mustNot(vBuilder);
        }


        if (!StringUtils.isEmpty(condition.getCategory())) {
            BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
            vBuilder.mustNot(QueryBuilders.termsQuery("category.keyword", condition.getCategory().split(",")));
            boolQueryBuilder.mustNot(vBuilder);
        }


        if (!StringUtils.isEmpty(condition.getVersion())) {
            BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
            vBuilder.mustNot(QueryBuilders.termsQuery("version.keyword", condition.getVersion().split(",")));
            boolQueryBuilder.mustNot(vBuilder);
        }

        if (!StringUtils.isEmpty(condition.getOs())) {
            BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
            vBuilder.mustNot(QueryBuilders.termsQuery("os.keyword", condition.getOs().split(",")));
            boolQueryBuilder.mustNot(vBuilder);
        }
        sourceBuilder.query(boolQueryBuilder);

        return sourceBuilder;

    }
}
