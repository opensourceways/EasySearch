package com.search.docsearch.service;

import com.search.docsearch.dto.software.SearchFindwordDto;
import com.search.docsearch.dto.software.SearchTagsDto;
import com.search.docsearch.entity.software.*;
import com.search.docsearch.except.ServiceException;

import java.util.List;
import java.util.Map;

public interface ISoftwareEsSearchService {

    SoftwareSearchResponce searchByCondition(SoftwareSearchCondition condition) throws ServiceException;

    List<SearchTagsDto> getTags(SoftwareSearchTags searchTags) throws ServiceException;

    List<SoftwareSearchCountResponce> getCountByCondition(SoftwareSearchCondition condition) throws ServiceException;

    List<SoftwareDocsAllResponce> searchAllByCondition(SoftwareSearchCondition condition) throws ServiceException;
}
