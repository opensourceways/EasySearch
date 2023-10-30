package com.search.EaseSearchsearch.controller;


import com.search.EaseSearchsearch.aop.LimitRequest;
import com.search.EaseSearchsearch.aop.LogAction;
import com.search.EaseSearchsearch.service.DivideService;
import com.search.EaseSearchsearch.service.ParameterVerification;
import com.search.EaseSearchsearch.vo.SearchDocs;
import com.search.EaseSearchsearch.vo.SysResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/search/sort")
public class DivideController {
    @Autowired
    private DivideService divideService;

    @Autowired
    private ParameterVerification parameterVerification;


    @LogAction(type = "Category search", OperationResource = "Other Documents")
    @PostMapping("/{type}")
    @LimitRequest(callTime = 1, callCount = 30)
    public SysResult DivideBLog(@PathVariable String type, @RequestBody Map<String, String> m) {
        try {
            if (!parameterVerification.typeVerification(type) || !parameterVerification.advancedSearchVerification(m)) {
                return SysResult.ParameterVerificationFailed();
            }
            Map<String, Object> result = divideService.advancedSearch(m, type);
            if (result == null) {
                return SysResult.fail("内容不存在", null);
            }
            return SysResult.ok("查询成功", result);
        } catch (Exception e) {
            log.error("advancedSearch error is: " + e.getMessage());
        }

        return SysResult.fail("查询失败", null);
    }

    @LogAction(type = "Category search", OperationResource = "Documents")
    @PostMapping("docs")
    @LimitRequest(callTime = 1, callCount = 30)
    public SysResult DivideDocs(@RequestBody @Validated SearchDocs searchDocs) {
        try {
            if (!parameterVerification.searchDocsVerification(searchDocs)) {
                return SysResult.ParameterVerificationFailed();
            }

            Map<String, Object> result = divideService.docsSearch(searchDocs);
            if (result == null) {
                return SysResult.fail("内容不存在", null);
            }
            return SysResult.ok("查询成功", result);
        } catch (Exception e) {
            log.error("docsSearch error is: " + e.getMessage());
            return SysResult.fail("查询失败", null);
        }
    }

}
