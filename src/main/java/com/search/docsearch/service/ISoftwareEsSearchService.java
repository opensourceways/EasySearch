package com.search.docsearch.service;



import com.search.docsearch.dto.software.SearchFindwordDto;
import com.search.docsearch.dto.software.SearchTagsDto;
import com.search.docsearch.entity.software.SoftwareSearchCondition;
import com.search.docsearch.entity.software.SoftwareSearchResponce;
import com.search.docsearch.entity.software.SoftwareSearchTags;
import com.search.docsearch.except.ServiceException;

import java.util.List;
import java.util.Map;


public interface ISoftwareEsSearchService {

    SoftwareSearchResponce searchByCondition(SoftwareSearchCondition condition) throws ServiceException;

    Map<String, Object> getCount(SoftwareSearchCondition condition) throws ServiceException;

    List<SearchTagsDto> getTags(SoftwareSearchTags searchTags)throws ServiceException;
    SearchFindwordDto findWord(String prefix, String dataType) throws ServiceException;
}
