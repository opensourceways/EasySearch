package com.search.docsearch.service;

import java.util.Map;

import com.search.docsearch.entity.vo.NpsBody;
import com.search.docsearch.entity.vo.SearchCondition;
import com.search.docsearch.entity.vo.SearchTags;
import com.search.docsearch.except.ServiceException;

public interface SearchService {

    Map<String, Object> getSuggestion(String keyword, String lang) throws ServiceException;

    Map<String, Object> searchByCondition(SearchCondition condition) throws ServiceException;

    Map<String, Object> getCount(SearchCondition condition) throws ServiceException;

    Map<String, Object> advancedSearch(Map<String, String> search) throws ServiceException;

    Map<String, Object> getTags(SearchTags searchTags) throws ServiceException;

    String querySigName(String lang) throws ServiceException;

    String queryAll() throws ServiceException;

    String querySigReadme(String sig, String lang) throws ServiceException;

    String getEcosystemRepoInfo(String ecosystemType, String sortType, String sortOrder, String page,
            String pageSize, String lang) throws ServiceException;
    
    String getNps(NpsBody body) throws Exception;

}
