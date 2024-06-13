package com.search.docsearch.controller.community;


import com.search.docsearch.aop.LimitRequest;
import com.search.docsearch.aop.LogAction;
import com.search.docsearch.config.EsfunctionScoreConfig;
import com.search.docsearch.entity.vo.SearchDocs;
import com.search.docsearch.entity.vo.SysResult;
import com.search.docsearch.service.DivideService;
import com.search.docsearch.utils.ParameterUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/search/sort")
@ConditionalOnProperty(name = "controller.enabled.community", havingValue = "true")
public class DivideController {
    @Autowired
    private DivideService divideService;
    @Autowired
    private EsfunctionScoreConfig esfunctionScoreConfig;

    @LogAction(type = "Category search", OperationResource = "Other Documents")
    @PostMapping("/{type}")
    @LimitRequest(callTime = 1, callCount = 30)
    public SysResult DivideBLog(@PathVariable @NotBlank(message = "must have a type") String type, @RequestBody @NotEmpty(message = "Requires at least one condition") Map<String, String> m) {

        try {
            ParameterUtil.vailAndLimitRequestMap(m,esfunctionScoreConfig.getEsExistingKey());
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
            Map<String, Object> result = divideService.docsSearch(searchDocs);
            if (result == null) {
                return SysResult.fail("内容不存在", null);
            }
            return SysResult.ok("查询成功", result);
        } catch (Exception e) {
            log.error("docsSearch error is: " + e.getMessage());
        }


        return SysResult.fail("查询失败", null);
    }

}
