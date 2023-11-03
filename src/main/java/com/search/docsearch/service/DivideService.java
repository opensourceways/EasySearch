package com.search.docsearch.service;

import com.search.docsearch.entity.vo.SearchDocs;
import com.search.docsearch.except.ServiceException;

import java.io.IOException;
import java.util.Map;

public interface DivideService {

    Map<String, Object> advancedSearch(Map<String, String> search, String category) throws ServiceException;

    Map<String, Object> docsSearch(SearchDocs searchDocs) throws ServiceException;
}
