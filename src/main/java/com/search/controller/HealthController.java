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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController implements ErrorController {
    /**
     * 路径.
     */
    private final String ERROR_PATH = "/error";

    /**
     * 健康检查.
     *
     * @param request  请求 .
     * @param response 响应.
     * @return String.
     */
    @RequestMapping(value = ERROR_PATH)
    public String errorHtml(HttpServletRequest request, HttpServletResponse response) {
        int code = response.getStatus();
        return "{\"code\":" + code + ",\"msg\": \"error\"}";
    }
}
