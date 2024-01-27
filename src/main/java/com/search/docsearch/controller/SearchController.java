package com.search.docsearch.controller;


import java.util.Map;

import com.search.docsearch.config.EsfunctionScoreConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.search.docsearch.aop.LimitRequest;
import com.search.docsearch.aop.LogAction;
import com.search.docsearch.config.MySystem;
import com.search.docsearch.entity.vo.NpsBody;
import com.search.docsearch.entity.vo.SearchCondition;
import com.search.docsearch.entity.vo.SearchTags;
import com.search.docsearch.entity.vo.SysCode;
import com.search.docsearch.entity.vo.SysResult;
import com.search.docsearch.except.ControllerException;
import com.search.docsearch.service.SearchService;
import com.search.docsearch.utils.ParameterUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;
    @Autowired
    @Qualifier("setConfig")
    private MySystem mySystem;

    @Autowired
    EsfunctionScoreConfig esfunctionScoreConfig;
    /**
     * 查询文档，首页大搜索
     *
     * @param condition 封装查询条件
     * @return 搜索结果
     */
    @LogAction(type = "Global search", OperationResource = "Documents")
    @PostMapping("docs")
    @LimitRequest()
    public SysResult searchDocByKeyword(@RequestBody @Validated SearchCondition condition) {
        ParameterUtil.vaildListMap(condition.getLimit());
        ParameterUtil.vaildListMap(condition.getFilter());
        try {
            Map<String, Object> result = searchService.searchByCondition(condition);
            if (result == null) {
                return SysResult.fail("内容不存在", null);
            }
            return SysResult.ok("查询成功", result);
        } catch (ControllerException e) {
            log.error("searchByCondition error is: " + e.getMessage());
        }
        return SysResult.fail("查询失败", null);
    }

    @LogAction(type = "Get aid", OperationResource = "Suggestion words")
    @PostMapping("sugg")
    @LimitRequest()
    public SysResult getSuggestion(@RequestBody @Validated SearchCondition condition) {
        ParameterUtil.vaildListMap(condition.getLimit());
        ParameterUtil.vaildListMap(condition.getFilter());
        if (!StringUtils.hasText(condition.getKeyword())) {
            return SysResult.fail("keyword must not null", null);
        }

        try {
            Map<String, Object> result = searchService.getSuggestion(condition.getKeyword(), condition.getLang());
            if (result == null) {
                return SysResult.fail("内容不存在", null);
            }
            return SysResult.ok("查询成功", result);
        } catch (ControllerException e) {
            log.error("getSuggestion error is: " + e.getMessage());
        }
        return SysResult.fail("查询失败", null);
    }


    @LogAction(type = "Get statistics", OperationResource = "Statistics data")
    @PostMapping("count")
    @LimitRequest()
    public SysResult getCount(@RequestBody @Validated SearchCondition condition) {
        ParameterUtil.vaildListMap(condition.getLimit());
        ParameterUtil.vaildListMap(condition.getFilter());
        try {
            Map<String, Object> result = searchService.getCount(condition);
            if (result == null) {
                return SysResult.fail("内容不存在", null);
            }
            return SysResult.ok("查询成功", result);
        } catch (ControllerException e) {
            log.error("getCount error is: " + e.getMessage());
        }
        return SysResult.fail("查询失败", null);
    }


    @LogAction(type = "Get aid", OperationResource = "Popular terms")
    @PostMapping("pop")
    @LimitRequest(callTime = 1, callCount = 1000)
    public SysResult getPop(String lang) {
        try {
            String[] result = null;
            if ("zh".equals(lang)) {
                result = new String[]{"迁移", "openGauss", "yum", "安装", "白皮书", "生命周期", "docker", "虚拟化"};
            } else if ("en".equals(lang)) {
                result = new String[]{"migration", "openGauss", "doc", "openstack", "cla"};
            } else {
                return SysResult.fail("Invalid lang parameter", null);
            }
            return SysResult.ok("查询成功", result);
        } catch (Exception e) {
            log.error("getPop error is: " + e.getMessage());
        }
        return SysResult.fail("查询失败", null);
    }


    @LogAction(type = "Filter documents", OperationResource = "Documents")
    @PostMapping("sort")
    @LimitRequest()
    public SysResult makeSort(@RequestBody Map<String, String> m) {
        try {
            ParameterUtil.vailAndLimitRequestMap(m,esfunctionScoreConfig.getEsExistingKey());
            Map<String, Object> result = searchService.advancedSearch(m);
            if (result == null) {
                return SysResult.fail("内容不存在", null);
            }
            return SysResult.ok("查询成功", result);
        } catch (ControllerException e) {
            log.error("advancedSearch error is: " + e.getMessage());
        }


        return SysResult.fail("查询失败", null);
    }

    @LogAction(type = "Get", OperationResource = "Tags")
    @PostMapping("tags")
    @LimitRequest()
    public SysResult getTags(@RequestBody @Validated SearchTags searchTags) {
        ParameterUtil.vaildMap(searchTags.getCondition());
        try {
            Map<String, Object> result = searchService.getTags(searchTags);
            if (result == null) {
                return SysResult.fail("内容不存在", null);
            }
            return SysResult.ok("查询成功", result);
        } catch (ControllerException e) {
            log.error("getTags error is: " + e.getMessage());
        }


        return SysResult.fail("查询失败", null);
    }

    @RequestMapping("sig/name")
    public String querySigName(@RequestParam(value = "lang", required = false) String lang) throws ControllerException {
        lang = ParameterUtil.vaildLang(lang);
        return searchService.querySigName(lang);
    }

    @RequestMapping("all")
    public String queryAll() throws ControllerException {
        return searchService.queryAll();
    }

    @RequestMapping("stars")
    public String queryStars() throws ControllerException {
        return searchService.queryStars();
    }

    @RequestMapping("sig/readme")
    public SysCode querySigReadme(@RequestParam(value = "sig") String sig,
                                  @RequestParam(value = "lang", required = false) String lang) throws ControllerException {
        lang = ParameterUtil.vaildLang(lang);
        String result = searchService.querySigReadme(sig, lang);
        return SysCode.ok("ok", result);
    }

    @RequestMapping(value = "ecosystem/repo/info")
    public String getEcosystemRepoInfo(@RequestParam(value = "ecosystem_type") String ecosystemType,
                                       @RequestParam(value = "lang", required = false) String lang,
                                       @RequestParam(value = "page", required = false) String page) throws ControllerException {
        lang = ParameterUtil.vaildLang(lang);
        ecosystemType = ParameterUtil.vaildEcosystemType(ecosystemType);
        page = ParameterUtil.vaildPage(page);
        return searchService.getEcosystemRepoInfo(ecosystemType, page, lang);
    }

    @PostMapping(value = "nps")
    public String getNps(@RequestParam(value = "community") String community, @RequestBody NpsBody body) throws ControllerException {
        return searchService.getNps(community, body);
    }

}
