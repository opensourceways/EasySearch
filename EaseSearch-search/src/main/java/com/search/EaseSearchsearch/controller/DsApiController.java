package com.search.EaseSearchsearch.controller;

import com.search.EaseSearchsearch.service.DsApiService;
import com.search.EaseSearchsearch.service.ParameterVerification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/search")
public class DsApiController {

    @Autowired
    private DsApiService dsApiService;

    @Autowired
    private ParameterVerification parameterVerification;




    @RequestMapping("sig/name")
    public String querySigName(@RequestParam(value = "lang", required = false) String lang) throws Exception {

        lang = parameterVerification.vaildLang(lang);
        return dsApiService.querySigName(lang);
    }

    @RequestMapping("all")
    public String queryAll() throws Exception {
        return dsApiService.queryAll();
    }

    @RequestMapping("sig/readme")
    public String querySigReadme(@RequestParam(value = "sig") String sig,
                                 @RequestParam(value = "lang", required = false) String lang) throws Exception {
        lang = parameterVerification.vaildLang(lang);
        return dsApiService.querySigReadme(sig, lang);
    }

    @RequestMapping(value = "ecosystem/repo/info")
    public String getEcosystemRepoInfo(@RequestParam(value = "ecosystem_type") String ecosystemType,
                                       @RequestParam(value = "lang", required = false) String lang,
                                       @RequestParam(value = "sort_type", required = false) String sortType,
                                       @RequestParam(value = "sort_order", required = false) String sortOrder,
                                       @RequestParam(value = "page", required = false) String page,
                                       @RequestParam(value = "pageSize", required = false) String pageSize) throws Exception {
        lang = parameterVerification.vaildLang(lang);
        ecosystemType = parameterVerification.vaildEcosystemType(ecosystemType);
        sortType = parameterVerification.vaildSortType(sortType);
        sortOrder = parameterVerification.vaildSortOrder(sortOrder);
        page = parameterVerification.vaildPage(page);
        pageSize = parameterVerification.vaildPageSize(pageSize);
        return dsApiService.getEcosystemRepoInfo(ecosystemType, sortType, sortOrder, page, pageSize, lang);
    }
}
