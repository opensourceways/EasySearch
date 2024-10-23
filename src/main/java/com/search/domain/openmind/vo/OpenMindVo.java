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
package com.search.domain.openmind.vo;

import com.search.domain.base.vo.CommunityBaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenMindVo extends CommunityBaseVo {
    /**
     * 文档类型.
     */
    private String docsType;
    /**
     * strong标签.
     */
    private String strong;

    /**
     * 文章名称.
     */
    private String articleName;
    /**
     * h1标签.
     */
    private String h1;
    /**
     * h2标签.
     */
    private String h2;
    /**
     * h3标签.
     */
    private String h3;
    /**
     * h4标签.
     */
    private String h4;
    /**
     * h5标签.
     */
    private String h5;
    /**
     * 类型.
     */
    private String type;
    /**
     * 版本.
     */
    private String version;
}
