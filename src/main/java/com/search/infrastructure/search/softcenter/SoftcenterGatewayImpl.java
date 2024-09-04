package com.search.infrastructure.search.softcenter;

import com.search.adapter.vo.CountResponceVo;
import com.search.adapter.vo.SoftWareVo;
import com.search.common.util.SortUtil;
import com.search.domain.base.vo.CountVo;
import com.search.domain.softcenter.dto.DocsSoftcenterCondition;
import com.search.domain.softcenter.gateway.SoftcenterGateway;
import com.search.domain.softcenter.vo.*;
import com.search.infrastructure.support.action.BaseFounctionGateway;

import com.search.infrastructure.support.converter.CommonConverter;
import com.search.infrastructure.search.softcenter.converter.SoftcenterConverter;
import com.search.infrastructure.search.softcenter.dataobject.*;
import com.search.infrastructure.search.softcenter.enums.SoftwareTypeEnum;
import com.search.infrastructure.search.softcenter.enums.SoftwarekeywordTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Slf4j
public class SoftcenterGatewayImpl extends BaseFounctionGateway implements SoftcenterGateway {
    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public SoftWareVo searchByCondition(DocsSoftcenterCondition condition) {
        SearchRequest searchRequest = requestBuilder.getDefaultSearchRequest(condition.getIndex());
        SearchSourceBuilder sourceBuilder = buildSearchSourceBuilderByCondition(condition);
        sourceBuilder.highlighter(requestBuilder.buildHighlightBuilder());
        sourceBuilder.from(condition.getPageFrom()).size(condition.getPageSize());
        sourceBuilder.timeout(TimeValue.timeValueMinutes(1L));
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = super.executeDefaultEsSearch(searchRequest);
        if (searchResponse != null) {
            List<Map<String, Object>> dataList = responceHandler.getDefaultsHightResponceToMapList(searchResponse, Arrays.asList("name", "summary"), "description");
            SoftWareVo searchResponce = getSearchResponce(dataList);
            return searchResponce;
        }
        return null;
    }


    @Override
    public CountResponceVo getSearchCountByCondition(DocsSoftcenterCondition condition) {
        List<CountVo> countList = new ArrayList<>();
        SearchRequest searchRequest = requestBuilder.getDefaultSearchRequest(condition.getIndex());
        SearchSourceBuilder sourceBuilder = buildSearchSourceBuilderByCondition(condition);

        sourceBuilder.aggregation(AggregationBuilders.terms("data").field("dataType.keyword")
                .subAggregation(AggregationBuilders.terms("category").field("category.keyword")));
        searchRequest.source(sourceBuilder);
        SearchResponse response = super.executeDefaultEsSearch(searchRequest);
        ParsedTerms aggregation = response.getAggregations().get("data");
        List<? extends Terms.Bucket> buckets = aggregation.getBuckets();
        HashMap<String, Long> docCountMap = new HashMap<>();
        if (buckets != null)
            for (Terms.Bucket bucket : buckets) {
                docCountMap.put(SoftwareTypeEnum.getFrontDeskTypeByType(bucket.getKeyAsString()), bucket.getDocCount());
            }
        for (SoftwareTypeEnum value : SoftwareTypeEnum.values()) {
            CountVo countVo = new CountVo();
            countVo.setDoc_count(
                    docCountMap.get(value.getFrontDeskType()) == null ? 0 : docCountMap.get(value.getFrontDeskType()));
            countVo.setKey(value.getFrontDeskType());
            countList.add(countVo);
        }
        CountResponceVo countResponceVo = new CountResponceVo();
        countResponceVo.setTotal(countList);
        return countResponceVo;
    }

    @Override
    public List<DocsAllVo> getSearchAllByCondition(DocsSoftcenterCondition condition) {
        List<DocsAllVo> responce = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(SoftwareTypeEnum.values().length - 2);
        for (SoftwareTypeEnum value : SoftwareTypeEnum.values()) {
            if (SoftwareTypeEnum.ALL.equals(value) || SoftwareTypeEnum.APPVERSION.equals(value))
                continue;

            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {

                        DocsSoftcenterCondition clone = condition.clone();
                        clone.setDataType(value.getFrontDeskType());
                        SoftWareVo softWareVo = searchByCondition(clone);
                        if (softWareVo.getTotal() > 0) {
                            List<NameDocsVo> nameList = new ArrayList<>();
                            switch (value) {
                                case APPLICATION:
                                    List<ApplicationPackageVo> apppkg = softWareVo.getApppkg();
                                    apppkg.stream().forEach(a -> {
                                        nameList.add(
                                                new NameDocsVo(a.getName(), a.getPkgId(), a.getVersion()));
                                    });
                                    break;

                                case RPMPKG:
                                    List<RPMPackageVo> rpmpkg = softWareVo.getRpmpkg();
                                    rpmpkg.stream().forEach(a -> {
                                        nameList.add(
                                                new NameDocsVo(a.getName(), a.getPkgId(), a.getVersion()));
                                    });
                                    break;

                                case EKPG:
                                    List<EPKGPackageVo> epkgpkg = softWareVo.getEpkgpkg();
                                    epkgpkg.stream().forEach(a -> {
                                        nameList.add(
                                                new NameDocsVo(a.getName(), a.getPkgId(), a.getVersion()));
                                    });
                                    break;

                            }
                            DocsAllVo docsAllVo = new DocsAllVo(
                                    value.getFrontDeskType(), softWareVo.getTotal(), nameList);
                            responce.add(docsAllVo);
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


    private SearchSourceBuilder buildSearchSourceBuilderByCondition(DocsSoftcenterCondition condition) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        String typeByfrontDeskType = SoftwareTypeEnum.getTypeByfrontDeskType(condition.getDataType());
        if (StringUtils.hasLength(typeByfrontDeskType)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("dataType.keyword", typeByfrontDeskType));
        }

        boolean isSupportKeywordType = SoftwarekeywordTypeEnum.isSupportKeywordType(condition.getKeywordType());
        boolean isSearchAll = "all".equals(condition.getKeywordType());

        if (isSearchAll || !isSupportKeywordType || "name".equals(condition.getKeywordType())) {

            MatchPhraseQueryBuilder titleMP = QueryBuilders.matchPhraseQuery("name", condition.getKeyword())
                    .analyzer("ik_max_word").slop(2);
            titleMP.boost(1000);

            WildcardQueryBuilder field = QueryBuilders.wildcardQuery("name", "*" + condition.getKeyword() + "*");
            boolQueryBuilder.should(field);
            boolQueryBuilder.should(titleMP);
        }

        if (isSearchAll || "file".equals(condition.getKeywordType())) {
            WildcardQueryBuilder providesWildcard = QueryBuilders.wildcardQuery("providesText.keyword",
                    "*" + condition.getKeyword() + "*");
            providesWildcard.boost(1);
            boolQueryBuilder.should(providesWildcard);
            WildcardQueryBuilder requiresWildcard = QueryBuilders.wildcardQuery("requiresText.keyword",
                    "*" + condition.getKeyword() + "*");
            requiresWildcard.boost(1);
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
        if ("asc".equals(condition.getNameOrder())) {
            sourceBuilder.sort("name.keyword", SortOrder.ASC);

        } else if ("desc".equals(condition.getNameOrder())) {
            sourceBuilder.sort("name.keyword", SortOrder.DESC);
        }
        return sourceBuilder;

    }


    private SoftWareVo getSearchResponce(List<Map<String, Object>> data) {
        SoftWareVo searchResponce = new SoftWareVo();
        Map<String, List<Map<String, Object>>> dataTypeMap = data.stream().collect(Collectors.groupingBy(m -> {
            return String.valueOf(m.get("dataType"));
        }));
        for (SoftwareTypeEnum value : SoftwareTypeEnum.values()) {
            List<Map<String, Object>> dateMapList = dataTypeMap.get(value.getType());

            List<SoftCenterDo> softCenterDos = CommonConverter.toDoList(dateMapList, SoftCenterDo.class);
            if (CollectionUtils.isEmpty(softCenterDos))
                continue;
            switch (value) {
                case APPLICATION:
                    searchResponce.setApppkg(SoftcenterConverter.convertToApplicationPackage(softCenterDos));
                    break;
                case RPMPKG:
                    searchResponce.setRpmpkg(SoftcenterConverter.convertAppMapToSoftwareRpmDto(softCenterDos));
                    break;

                case EKPG:
                    searchResponce.setEpkgpkg(SoftcenterConverter.convertToEPKGPackage(softCenterDos));
                    break;

                case ALL:
                    searchResponce.setAll(SoftcenterConverter.convertToFieldApplication(softCenterDos));

                case APPVERSION:
                    searchResponce.setAppversion(SoftcenterConverter.convertToApplicationVersion(softCenterDos));
                    break;
            }
        }
        return searchResponce;
    }
}
