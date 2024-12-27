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
import com.search.docsearch.thread.ThreadLocalCache;
import com.search.docsearch.utils.JacksonUtils;
import com.search.docsearch.utils.SortUtil;
import com.search.docsearch.utils.Trie;

import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@Slf4j
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
        if (response == null)
            return null;

        SoftwareSearchResponce softwareSearchResponce = handEsResponce(response);
        if (condition.getNameOrder() != null) {
            SortUtil.sortByName(softwareSearchResponce,condition);
        }

        if (condition.getTimeOrder() != null) {
            SortUtil.sortByTime(softwareSearchResponce, condition.getTimeOrder());
        }

        if (softwareSearchResponce.getTotal() > 10000) {
           /* List<SoftwareSearchCountResponce> countByCondition = getCountByCondition(condition);
            long total = 0l;
            for (SoftwareSearchCountResponce softwareSearchCountResponce : countByCondition) {
                total += softwareSearchCountResponce.getDocCount();
            }*/
            softwareSearchResponce.setTotal(10000);
        }
        return softwareSearchResponce;

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
        sourceBuilder.aggregation(AggregationBuilders.terms("data").field(searchTags.getWant() + ".keyword")
                .size(Constants.MAX_ES_RETURN_LENGHTH)
                .order(bucketOrder));
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
    public List<SoftwareSearchCountResponce> getCountByCondition(SoftwareSearchCondition condition)
            throws ServiceException {
        List<SoftwareSearchCountResponce> countList = new ArrayList<>();
        SearchRequest request = new SearchRequest(searchConfig.getIndex());
        SearchSourceBuilder searchSourceBuilder = buildSearchSourceBuilderByCondition(condition);
        searchSourceBuilder.aggregation(AggregationBuilders.terms("data").field("dataType.keyword")
                .subAggregation(AggregationBuilders.terms("category").field("category.keyword")));
        request.source(searchSourceBuilder);
        SearchResponse response = null;

        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ServiceImplException("can not search");
        }

        ParsedTerms aggregation = response.getAggregations().get("data");
        List<? extends Terms.Bucket> buckets = aggregation.getBuckets();
        HashMap<String, Long> docCountMap = new HashMap<>();
        if (buckets != null)
            for (Terms.Bucket bucket : buckets) {
                docCountMap.put(SoftwareTypeEnum.getFrontDeskTypeByType(bucket.getKeyAsString()), bucket.getDocCount());
            }
        Boolean isTokenEmpty = StringUtils.isEmpty(ThreadLocalCache.get());

        for (SoftwareTypeEnum value : SoftwareTypeEnum.values()) {
            if (SoftwareTypeEnum.APPVERSION.equals(value) && isTokenEmpty) {
                continue;
            }
            SoftwareSearchCountResponce softwareSearchCountResponce = new SoftwareSearchCountResponce();
            softwareSearchCountResponce.setDocCount(
                    docCountMap.get(value.getFrontDeskType()) == null ? 0 : docCountMap.get(value.getFrontDeskType()) > 10000 ? 10000 : docCountMap.get(value.getFrontDeskType()));
            softwareSearchCountResponce.setKey(value.getFrontDeskType());
            countList.add(softwareSearchCountResponce);
        }

        return countList;
    }

    @Override
    public List<SoftwareDocsAllResponce> searchAllByCondition(SoftwareSearchCondition condition)
            throws ServiceException {
        if (condition.getKeywordType() == null) {
            condition.setKeywordType("all");
        }
        List<SoftwareDocsAllResponce> responce = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(SoftwareTypeEnum.values().length - 2);
        for (SoftwareTypeEnum value : SoftwareTypeEnum.values()) {
            if (SoftwareTypeEnum.ALL.equals(value) || SoftwareTypeEnum.APPVERSION.equals(value))
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
                                        nameList.add(
                                                new SoftwareNameDocsDto(a.getName(), a.getPkgId(), a.getAppVer()));
                                    });
                                    break;

                                case RPMPKG:
                                    List<SoftwareRpmDto> rpmpkg = softwareSearchResponce.getRpmpkg();
                                    rpmpkg.stream().forEach(a -> {
                                        nameList.add(
                                                new SoftwareNameDocsDto(a.getName(), a.getPkgId(), a.getVersion()));
                                    });
                                    break;

                               /* case EKPG:
                                    List<SoftwareEpkgDto> epkgpkg = softwareSearchResponce.getEpkgpkg();
                                    epkgpkg.stream().forEach(a -> {
                                        nameList.add(
                                                new SoftwareNameDocsDto(a.getName(), a.getPkgId(), a.getVersion()));
                                    });
                                    break;*/

                                case OEPKG:
                                    List<SoftwareOepkgDto> oepkg = softwareSearchResponce.getOepkg();
                                    oepkg.stream().forEach(a -> {
                                        nameList.add(
                                                new SoftwareNameDocsDto(a.getName(), a.getPkgId(), a.getVersion()));
                                    });
                                    break;

                            }
                            SoftwareDocsAllResponce softwareDocsAllResponce = new SoftwareDocsAllResponce(
                                    value.getFrontDeskType(), softwareSearchResponce.getTotal() > 10000 ? 10000 : softwareSearchResponce.getTotal(), nameList);
                            responce.add(softwareDocsAllResponce);
                        }

                    } catch (Exception e) {
                        log.error("erorr happened in searchAllByCondition");
                    } finally {
                        countDownLatch.countDown();
                    }
                }

            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("countDownLatch await failed");
        }
        if (responce.size() > 0) {
            SortUtil.sortResponce(responce);
        }

        return responce;
    }

    private SoftwareSearchResponce handEsResponce(SearchResponse response) {
        List<Map<String, Object>> data = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();
            String description = (String) map.getOrDefault("description", "");
            if (null != description && description.length() > Constants.MAX_ES_DESC_LENGHTH) {
                description = description.substring(0, Constants.MAX_ES_DESC_LENGHTH) + "......";
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

               /* case EKPG:
                    searchResponce.setEpkgpkg(convertAppMapToSoftwareEpkgDto(maps));
                    break;*/

                case ALL:
                    searchResponce.setAll(convertAllMapToSoftwareDto(maps));
                    break;

                case OEPKG:
                    searchResponce.setOepkg(convertOEpkgMapToSoftwareDto(maps));
                    break;
                case APPVERSION:
                    if (StringUtils.isEmpty(ThreadLocalCache.get())) {
                        break;
                    }
                    searchResponce.setAppversion(convertAppversionMapToSoftwareDto(maps));
                    break;
            }
        }
        return searchResponce;
    }


    private List<SoftwareOepkgDto> convertOEpkgMapToSoftwareDto(List<Map<String, Object>> maps) {
        List<SoftwareOepkgDto> softwareOEpkgDtoList = new ArrayList<>();
        maps.stream().forEach(m -> {
                    try {
                        SoftwareOepkgDto oepkgDto = JacksonUtils.toObject(SoftwareOepkgDto.class,
                                JSONObject.toJSONString(m));
                        softwareOEpkgDtoList.add(oepkgDto);
                    } catch (Exception e) {
                        log.error("error happens in convertAppversionMapToSoftwareDto");
                    }
                }

        );
        return softwareOEpkgDtoList;
    }

    private List<SoftwareAppVersionDto> convertAppversionMapToSoftwareDto(List<Map<String, Object>> maps) {
        List<SoftwareAppVersionDto> softwareAppVersionDtoList = new ArrayList<>();
        maps.stream().forEach(m -> {
                    try {
                        SoftwareAppVersionDto softwareAppVersionDto = JacksonUtils.toObject(SoftwareAppVersionDto.class,
                                JSONObject.toJSONString(m));
                        softwareAppVersionDtoList.add(softwareAppVersionDto);
                    } catch (Exception e) {
                        log.error("error happens in convertAppversionMapToSoftwareDto");
                    }
                }

        );
        return softwareAppVersionDtoList;
    }

    private List<SoftwareAllDto> convertAllMapToSoftwareDto(List<Map<String, Object>> maps) {
        List<SoftwareAllDto> softwareAllDtoList = new ArrayList<>();
        maps.stream().forEach(m -> {
                    try {
                        if (m.containsKey("pkgIds")) {
                            m.put("pkgIds", JSONObject.parseObject(m.get("pkgIds") + ""));

                        }
                        if (m.containsKey("maintainers")) {
                            m.put("maintainers", JSONObject.parseObject(m.get("maintainers") + ""));

                        }
                        if (m.containsKey("tags")) {
                            m.put("tags", SortUtil.sortTags(JSONObject.parseArray(m.get("tags") + "")));

                        }
                        SoftwareAllDto softwareAllDto = JacksonUtils.toObject(SoftwareAllDto.class, JSONObject.toJSONString(m));
                        softwareAllDtoList.add(softwareAllDto);
                    } catch (Exception e) {
                        log.error("error happens in convertAllMapToSoftwareDto");
                    }
                }

        );
        return softwareAllDtoList;
    }

    private List<SoftwareAppChildrenDto> convertAppMapToSoftwareDto(List<Map<String, Object>> maps) {
        List<SoftwareAppChildrenDto> softwareAppDtoList = new ArrayList<>();
        maps.stream().forEach(m -> {
                    try {
                        SoftwareAppChildrenDto softwareAppChildrenDto = JacksonUtils.toObject(SoftwareAppChildrenDto.class,
                                JSONObject.toJSONString(m));
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
                    } catch (Exception e) {
                        log.error("error happens in convertAppMapToSoftwareDto");
                    }
                }

        );
        return softwareAppDtoList;
    }

    private List<SoftwareEpkgDto> convertAppMapToSoftwareEpkgDto(List<Map<String, Object>> maps) {
        List<SoftwareEpkgDto> softwareEpkgDtoList = new ArrayList<>();

        maps.stream().forEach(m -> {
            try {
                m.put("epkgUpdateAt", m.get("updatetime"));
                m.put("epkgCategory", m.get("category"));
                m.put("epkgSize", m.get("size"));
                softwareEpkgDtoList.add(JacksonUtils.toObject(SoftwareEpkgDto.class, JSONObject.toJSONString(m)));
            } catch (Exception e) {
                log.error("error happens in convertAppMapToSoftwareEpkgDto");
            }
        });
        return softwareEpkgDtoList;
    }

    private List<SoftwareRpmDto> convertAppMapToSoftwareRpmDto(List<Map<String, Object>> maps) {
        List<SoftwareRpmDto> softwareAppDtoList = new ArrayList<>();

        maps.stream().forEach(m -> {
            try {
                m.put("rpmUpdateAt", m.get("updatetime"));
                m.put("rpmCategory", m.get("category"));
                m.put("rpmSize", m.get("size"));
                softwareAppDtoList.add(JacksonUtils.toObject(SoftwareRpmDto.class, JSONObject.toJSONString(m)));
            } catch (Exception e) {
                log.error("error happens inconvertAppMapToSoftwareRpmDto");
            }
        });
        return softwareAppDtoList;
    }

    private SearchRequest buildSearchRequest(SoftwareSearchCondition condition, String index) {
        int startIndex = (condition.getPageNum() - 1) * condition.getPageSize();
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = buildSearchSourceBuilderByCondition(condition);
        sourceBuilder.from(startIndex);
        buildHighlightBuilder(sourceBuilder);

        if ("asc".equals(condition.getNameOrder()) || "desc".equals(condition.getNameOrder())) {
            sourceBuilder.from(0);
            sourceBuilder.size(100);
        } else if (startIndex + condition.getPageSize() > 10000) {
            sourceBuilder.size(10000 - startIndex);
        } else {
            sourceBuilder.size(condition.getPageSize());
        }
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
        if (StringUtils.hasLength(typeByfrontDeskType)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("dataType.keyword", typeByfrontDeskType));
        }

        boolean isSupportKeywordType = SoftwarekeywordTypeEnum.isSupportKeywordType(condition.getKeywordType());
        boolean isSearchAll = "all".equals(condition.getKeywordType());

        MatchPhraseQueryBuilder titleMP = QueryBuilders.matchPhraseQuery("name", condition.getKeyword())
                .analyzer("ik_max_word").slop(2);
        titleMP.boost(100);


        WildcardQueryBuilder lowerNameMP = QueryBuilders.wildcardQuery("name", "*" + condition.getKeyword().toLowerCase(Locale.ROOT) + "*");
        lowerNameMP.boost(10);

        WildcardQueryBuilder upNameMP = QueryBuilders.wildcardQuery("name", "*" + condition.getKeyword().toUpperCase(Locale.ROOT) + "*");
        upNameMP.boost(10);
        if (isSearchAll || !isSupportKeywordType || "name".equals(condition.getKeywordType())) {
            boolQueryBuilder.should(lowerNameMP);
            boolQueryBuilder.should(upNameMP);
            boolQueryBuilder.should(titleMP);

            // 软件中心根据版本号进行搜索
            WildcardQueryBuilder versionMP = QueryBuilders.wildcardQuery("version.keyword", "*" + condition.getKeyword() + "*");
            versionMP.boost(10);
            boolQueryBuilder.should(versionMP);
        }

        if (isSearchAll || "ubuntu".equals(condition.getKeywordType())) {
            MatchPhraseQueryBuilder originPkg = QueryBuilders.matchPhraseQuery("originPkg", condition.getKeyword())
                    .analyzer("ik_max_word").slop(2);
            boolQueryBuilder.should(originPkg);

        }

        if (isSearchAll || "file".equals(condition.getKeywordType())) {
            WildcardQueryBuilder providesWildcard = QueryBuilders.wildcardQuery("providesText.keyword",
                    "*" + condition.getKeyword() + "*");
            providesWildcard.boost(3);
            boolQueryBuilder.should(providesWildcard);
            WildcardQueryBuilder requiresWildcard = QueryBuilders.wildcardQuery("requiresText.keyword",
                    "*" + condition.getKeyword() + "*");
            requiresWildcard.boost(3);
            boolQueryBuilder.should(requiresWildcard);
        }

        if (isSearchAll || "description".equals(condition.getKeywordType())) {
            MatchPhraseQueryBuilder descriptionBuilder = QueryBuilders
                    .matchPhraseQuery("description", condition.getKeyword()).analyzer("ik_max_word");
            descriptionBuilder.boost(10);
            boolQueryBuilder.should(descriptionBuilder);
        }

        if (isSearchAll || "summary".equals(condition.getKeywordType())) {
            MatchPhraseQueryBuilder summaryBuilder = QueryBuilders.matchPhraseQuery("summary", condition.getKeyword())
                    .analyzer("ik_max_word");
            summaryBuilder.boost(10);
            boolQueryBuilder.should(summaryBuilder);
        }
        boolQueryBuilder.minimumShouldMatch(1);

        if (StringUtils.hasLength(condition.getArch())) {
            BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
            vBuilder.mustNot(QueryBuilders.termsQuery("arch.keyword", condition.getArch().split(",")));
            boolQueryBuilder.mustNot(vBuilder);
        }

        if (StringUtils.hasLength(condition.getEulerOsVersion())) {
            BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
            vBuilder.mustNot(QueryBuilders.termsQuery("eulerOsVersion.keyword", condition.getEulerOsVersion()));
            boolQueryBuilder.mustNot(vBuilder);
        }

        if (StringUtils.hasLength(condition.getCategory())) {
            BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
            vBuilder.mustNot(QueryBuilders.termsQuery("category.keyword", condition.getCategory().split(",")));
            boolQueryBuilder.mustNot(vBuilder);
        }

        if (StringUtils.hasLength(condition.getVersion())) {
            BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
            vBuilder.mustNot(QueryBuilders.termsQuery("version.keyword", condition.getVersion().split(",")));
            boolQueryBuilder.mustNot(vBuilder);
        }

        if (StringUtils.hasLength(condition.getOs())) {
            BoolQueryBuilder vBuilder = QueryBuilders.boolQuery();
            vBuilder.mustNot(QueryBuilders.termsQuery("os.keyword", condition.getOs().split(",")));
            boolQueryBuilder.mustNot(vBuilder);
        }
        sourceBuilder.query(boolQueryBuilder);
       /* if ("asc".equals(condition.getNameOrder())) {
            sourceBuilder.sort("name.keyword", SortOrder.ASC);

        } else if ("desc".equals(condition.getNameOrder())) {
            sourceBuilder.sort("name.keyword", SortOrder.DESC);
        }*/
        return sourceBuilder;

    }
}
