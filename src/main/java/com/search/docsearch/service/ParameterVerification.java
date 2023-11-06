package com.search.docsearch.service;

import com.search.docsearch.entity.vo.SearchCondition;
import com.search.docsearch.entity.vo.SearchDocs;
import com.search.docsearch.entity.vo.SearchTags;

import java.util.Map;

public interface ParameterVerification {

    Boolean conditionVerification(SearchCondition condition);

    Boolean langVerification(String lang);

    Boolean searchDocsVerification(SearchDocs searchDocs);

    Boolean paginationVerification(int page, int pageSize);

    Boolean typeVerification(String type);

    Boolean advancedSearchVerification(Map<String, String> search) throws Exception;

    Boolean searchTagsVerification(SearchTags searchTags);

    Boolean mapVerification(Map<String, String> m);


    String vaildLang(String lang);

    String vaildCommunity(String community);

    String vaildEcosystemType(String ecosystemType);

    String vaildSortType(String sortType);

    String vaildSortOrder(String sortOrder);

    String vaildPage(String page);

    String vaildPageSize(String pageSize);
}
