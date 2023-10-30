package com.search.EaseSearchsearch.service;

import com.search.EaseSearchsearch.vo.SearchDocs;

import java.io.IOException;
import java.util.Map;

public interface DivideService {

    Map<String, Object> advancedSearch(Map<String, String> search, String category) throws Exception;

    Map<String, Object> docsSearch(SearchDocs searchDocs) throws IOException;
}
