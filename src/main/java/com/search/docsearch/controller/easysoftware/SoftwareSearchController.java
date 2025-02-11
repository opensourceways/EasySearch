package com.search.docsearch.controller.easysoftware;

import java.util.List;
import com.search.docsearch.aop.LogAction;
import com.search.docsearch.aop.RequestLimitRedis;

import com.search.docsearch.entity.software.*;
import com.search.docsearch.enums.QueryTyepEnum;
import com.search.docsearch.recognition.RecognitionService;
import com.search.docsearch.service.ISoftwareEsSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/software")
@ConditionalOnProperty(name = "controller.enabled.easysoftware", havingValue = "true")
public class SoftwareSearchController {

    @Autowired
    private ISoftwareEsSearchService searchService;

    @Autowired
    private RecognitionService recgService;

    @LogAction(type = "software Global search", OperationResource = "Documents")
    @PostMapping("docs")
    @RequestLimitRedis()
    public SoftwareSysResult searchByKeyword(@RequestBody @Validated SoftwareSearchCondition condition) {

        try {
            // preprocess of query
            QueryTyepEnum queryType = recgService.ClassifyQuery(condition);
            SoftwareSearchCondition clsCondition = recgService.ProcessQuery(condition, queryType);
            SoftwareSearchResponce result = searchService.searchByCondition(clsCondition);
            if (result == null) {
                return SoftwareSysResult.fail("内容不存在", null);
            }
            return SoftwareSysResult.ok("查询成功", result);
        } catch (Exception e) {
            log.error("searchByCondition failed");
        }
        return SoftwareSysResult.fail("查询失败", null);
    }

    @PostMapping("count")
    @RequestLimitRedis()
    public SoftwareSysResult getCount(@RequestBody @Validated SoftwareSearchCondition condition) {
        try {
            // preprocess of query
            QueryTyepEnum queryType = recgService.ClassifyQuery(condition);
            SoftwareSearchCondition clsCondition = recgService.ProcessQuery(condition, queryType);
            List<SoftwareSearchCountResponce> result = searchService.getCountByCondition(clsCondition);
            if (result == null) {
                return SoftwareSysResult.fail("内容不存在", null);
            }
            return SoftwareSysResult.ok("查询成功", result);
        } catch (Exception e) {
            log.error("getCount failed");
        }

        return SoftwareSysResult.fail("查询失败", null);
    }

    @PostMapping("docsAll")
    @RequestLimitRedis()
    public SoftwareSysResult searchAllByKeyword(@RequestBody @Validated SoftwareSearchCondition condition) {

        try {
            QueryTyepEnum queryType = recgService.ClassifyQuery(condition);
            SoftwareSearchCondition clsCondition = recgService.ProcessQuery(condition, queryType);
            List<SoftwareDocsAllResponce> result = searchService.searchAllByCondition(clsCondition);

            if (result == null) {
                return SoftwareSysResult.fail("内容不存在", null);
            }
            return SoftwareSysResult.ok("查询成功", result);
        } catch (Exception e) {
            log.error("searchAllByKeyword failed");
        }
        return SoftwareSysResult.fail("查询失败", null);
    }
}
