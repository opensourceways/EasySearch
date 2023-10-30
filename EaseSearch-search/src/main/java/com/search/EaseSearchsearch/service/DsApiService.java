package com.search.EaseSearchsearch.service;

public interface DsApiService {
    String querySigName(String lang) throws Exception;

    String queryAll() throws Exception;

    String querySigReadme(String sig, String lang) throws Exception;

    String getEcosystemRepoInfo(String ecosystemType, String sortType, String sortOrder, String page, String pageSize, String lang) throws Exception;
}
