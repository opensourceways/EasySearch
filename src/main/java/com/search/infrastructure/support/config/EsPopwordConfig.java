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
package com.search.infrastructure.support.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "elasticsearch")
public class EsPopwordConfig {
    /**
     * 多个社区热词配置.
     */
    List<Pop> pop;

    /**
     * MatchQuery.
     */
    @Getter
    @Setter
    public static class Pop {
        /**
         * 数据源.
         */
        private String source;
        /**
         * 语言.
         */
        private String lang;
        /**
         * popword.
         */
        private String word;
        /**
         * 随机返回的热词数量.
         */
        private Integer num;
    }
}
