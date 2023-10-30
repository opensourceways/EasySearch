package com.search.EaseSearchsearch.controller;


import com.search.EaseSearchsearch.aop.LimitRequest;
import com.search.EaseSearchsearch.aop.LogAction;
import com.search.EaseSearchsearch.service.ParameterVerification;
import com.search.EaseSearchsearch.service.SearchService;
import com.search.EaseSearchsearch.vo.SearchCondition;
import com.search.EaseSearchsearch.vo.SearchTags;
import com.search.EaseSearchsearch.vo.SysResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private ParameterVerification parameterVerification;

    @LogAction(type = "Global search", OperationResource = "Documents")
    @PostMapping("docs")
    @LimitRequest()
    public SysResult searchDocByKeyword(@RequestBody SearchCondition condition) {
        try {
            if (!parameterVerification.conditionVerification(condition)) {
                return SysResult.ParameterVerificationFailed();
            }

            Map<String, Object> result = searchService.searchByCondition(condition);
            if (result == null) {
                return SysResult.fail("内容不存在", null);
            }
            return SysResult.ok("查询成功", result);
        } catch (Exception e) {
            log.error("searchByCondition error is: " + e.getMessage());
            return SysResult.fail("查询失败", null);
        }

    }

    @LogAction(type = "Get aid", OperationResource = "Suggestion words")
    @PostMapping("sugg")
    @LimitRequest()
    public SysResult getSuggestion(@RequestBody @Validated SearchCondition condition) {
        try {
            if (!parameterVerification.conditionVerification(condition)) {
                return SysResult.ParameterVerificationFailed();
            }

            Map<String, Object> result = searchService.getSuggestion(condition.getKeyword(), condition.getLang());
            if (result == null) {
                return SysResult.fail("内容不存在", null);
            }
            return SysResult.ok("查询成功", result);
        } catch (IOException e) {
            log.error("getSuggestion error is: " + e.getMessage());
            return SysResult.fail("查询失败", null);
        }

    }

    @LogAction(type = "Get statistics", OperationResource = "Statistics data")
    @PostMapping("count")
    @LimitRequest()
    public SysResult getCount(@RequestBody @Validated SearchCondition condition) {
        try {
            if (!parameterVerification.conditionVerification(condition)) {
                return SysResult.ParameterVerificationFailed();
            }

            Map<String, Object> result = searchService.getCount(condition);
            if (result == null) {
                return SysResult.fail("内容不存在", null);
            }
            return SysResult.ok("查询成功", result);
        } catch (Exception e) {
            log.error("getCount error is: " + e.getMessage());
            return SysResult.fail("查询失败", null);
        }

    }

    @LogAction(type = "Get aid", OperationResource = "Popular terms")
    @PostMapping("pop")
    @LimitRequest(callTime = 1, callCount = 1000)
    public SysResult getPop(String lang) {
        try {
            if (!parameterVerification.langVerification(lang)) {
                return SysResult.ParameterVerificationFailed();
            }

            String[] result = null;
            if (lang.equals("zh")) {
                result = new String[]{"迁移", "openGauss", "yum", "安装", "白皮书", "生命周期", "docker", "虚拟化"};
            } else {
                result = new String[]{"migration", "openGauss", "doc", "openstack", "cla"};
            }

            return SysResult.ok("查询成功", result);
        } catch (Exception e) {
            log.error("getPop error is: " + e.getMessage());
            return SysResult.fail("查询失败", null);
        }
    }


    @LogAction(type = "Filter documents", OperationResource = "Documents")
    @PostMapping("sort")
    @LimitRequest()
    public SysResult makeSort(@RequestBody Map<String, String> m) {
        try {
            if (!parameterVerification.advancedSearchVerification(m)) {
                return SysResult.ParameterVerificationFailed();
            }

            Map<String, Object> result = searchService.advancedSearch(m);
            if (result == null) {
                return SysResult.fail("内容不存在", null);
            }
            return SysResult.ok("查询成功", result);
        } catch (Exception e) {
            log.error("advancedSearch error is: " + e.getMessage());
            return SysResult.fail("查询失败", null);
        }
    }

    @LogAction(type = "Get", OperationResource = "Tags")
    @PostMapping("tags")
    @LimitRequest()
    public SysResult getTags(@RequestBody SearchTags searchTags) {
        try {
            if (!parameterVerification.searchTagsVerification(searchTags)) {
                return SysResult.ParameterVerificationFailed();
            }

            Map<String, Object> result = searchService.getTags(searchTags);
            if (result == null) {
                return SysResult.fail("内容不存在", null);
            }
            return SysResult.ok("查询成功", result);
        } catch (Exception e) {
            log.error("getTags error is: " + e.getMessage());
            return SysResult.fail("查询失败", null);
        }
    }



}
