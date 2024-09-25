/* Copyright (c) 2024 openEuler Community
 EasySoftware is licensed under the Mulan PSL v2.
 You can use this software according to the terms and conditions of the Mulan PSL v2.
 You may obtain a copy of Mulan PSL v2 at:
     http://license.coscl.org.cn/MulanPSL2
 THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 See the Mulan PSL v2 for more details.
*/
package com.search.controller;

import com.search.adapter.condition.NpsConditon;
import com.search.common.entity.JumperResult;
import com.search.common.util.ParameterUtil;
import com.search.infrastructure.openapi.gateway.JumperGatewayImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class JumperController {
    /**
     * Autowired jumperGateway bean.
     */
    @Autowired
    JumperGatewayImpl jumperGateway;

    /**
     * jump request.
     *
     * @param lang 语言 .
     * @return String.
     */
    @RequestMapping("sig/name")
    public String querySigName(@RequestParam(value = "lang", required = false) String lang) {
        lang = ParameterUtil.vaildLang(lang);
        return jumperGateway.querySigName(lang);
    }

    /**
     * jump request.
     *
     * @return String.
     */
    @RequestMapping("all")
    public String queryAll() {
        return jumperGateway.queryAll();
    }

    /**
     * jump request.
     *
     * @return String.
     */
    @RequestMapping("stars")
    public String queryStars() {
        return jumperGateway.queryStars();
    }

    /**
     * jump request.
     *
     * @param lang 语言 .
     * @param sig  sig .
     * @return JumperResult.
     */
    @RequestMapping("sig/readme")
    public JumperResult querySigReadme(@RequestParam(value = "sig") String sig,
                                       @RequestParam(value = "lang", required = false) String lang) {
        lang = ParameterUtil.vaildLang(lang);
        String result = jumperGateway.querySigReadme(sig, lang);
        return JumperResult.ok("ok", result);
    }

    /**
     * jump request.
     *
     * @param lang          语言 .
     * @param ecosystemType ecosystemType .
     * @param page          page .
     * @return String.
     */
    @RequestMapping(value = "ecosystem/repo/info")
    public String getEcosystemRepoInfo(@RequestParam(value = "ecosystem_type") String ecosystemType,
                                       @RequestParam(value = "lang", required = false) String lang,
                                       @RequestParam(value = "page", required = false) String page) {
        lang = ParameterUtil.vaildLang(lang);
        ecosystemType = ParameterUtil.vaildEcosystemType(ecosystemType);
        page = ParameterUtil.vaildPage(page);
        return jumperGateway.getEcosystemRepoInfo(ecosystemType, page, lang);
    }

    /**
     * jump request.
     *
     * @param community 社区 .
     * @param body      body .
     * @return String.
     */
    @PostMapping(value = "nps")
    public String getNps(@RequestParam(value = "community") String community, @RequestBody NpsConditon body) {
        return jumperGateway.getNps(community, body);
    }
}
