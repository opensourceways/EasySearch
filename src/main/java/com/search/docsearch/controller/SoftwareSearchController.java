package com.search.docsearch.controller;

import java.util.List;
import java.util.Map;

import com.search.docsearch.aop.LogAction;
import com.search.docsearch.dto.software.SearchFindwordDto;
import com.search.docsearch.dto.software.SearchTagsDto;
import com.search.docsearch.entity.software.*;
import com.search.docsearch.service.ISoftwareEsSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/software")
public class SoftwareSearchController {

    @Autowired
    private ISoftwareEsSearchService searchService;

    @LogAction(type = "software Global search", OperationResource = "Documents")
    @PostMapping("docs")
    public SoftwareSysResult searchByKeyword(@RequestBody @Validated SoftwareSearchCondition condition) {

        try {
            SoftwareSearchResponce result = searchService.searchByCondition(condition);
            log.info("result:" + result);
            if (result == null) {
                return SoftwareSysResult.fail("内容不存在", null);
            }
            return SoftwareSysResult.ok("查询成功", result);
        } catch (Exception e) {
            log.error("searchByCondition error is: " + e.getMessage());
        }
        return SoftwareSysResult.fail("查询失败", null);
    }


    @PostMapping("word")
    @LogAction(type = "Get", OperationResource = "word")
    public SoftwareSysResult findWord(@RequestParam(value = "query") String query, @RequestParam(value = "dataType", required = false) String dataType) {

        try {
            SearchFindwordDto word = searchService.findWord(query, dataType);
            if (word == null) {
                return SoftwareSysResult.fail("内容不存在", null);
            }
            return SoftwareSysResult.ok("查询成功", word);
        } catch (Exception e) {
            log.error("getCount error is: " + e.getMessage());
        }
        return SoftwareSysResult.fail("查询失败", null);
    }

    @PostMapping("tags")
    public SoftwareSysResult getTags(@RequestBody @Validated SoftwareSearchTags searchTags) {
        try {
            List<SearchTagsDto> result = searchService.getTags(searchTags);
            if (result == null) {
                return SoftwareSysResult.fail("内容不存在", null);
            }
            return SoftwareSysResult.ok("查询成功", result);
        } catch (Exception e) {
            log.error("getTags error is: " + e.getMessage());
        }


        return SoftwareSysResult.fail("查询失败", null);
    }
    @PostMapping("count")
    public SoftwareSysResult getCount(@RequestBody @Validated SoftwareSearchCondition condition) {
        try {
            List<SoftwareSearchCountResponce> result = searchService.getCountByCondition(condition);
            if (result == null) {
                return SoftwareSysResult.fail("内容不存在", null);
            }
            return SoftwareSysResult.ok("查询成功", result);
        } catch (Exception e) {
            log.error("getTags error is: " + e.getMessage());
        }


        return SoftwareSysResult.fail("查询失败", null);
    }

    @PostMapping("docsAll")
    public SoftwareSysResult searchAllByKeyword(@RequestBody @Validated SoftwareSearchCondition condition) {

        try {
            List<SoftwareDocsAllResponce> result = searchService.searchAllByCondition(condition);
            if (result == null) {
                return SoftwareSysResult.fail("内容不存在", null);
            }
            return SoftwareSysResult.ok("查询成功", result);
        } catch (Exception e) {
            log.error("searchAllByKeyword error is: " + e.getMessage());
        }
        return SoftwareSysResult.fail("查询失败", null);
    }
}


