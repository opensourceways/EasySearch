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
package com.search.domain.base.vo;


import lombok.Data;

@Data
public class CommunityCssVo extends CommunityBaseVo {
    /**
     * css h1 标签.
     */
    private String h1;
    /**
     * css h2 标签.
     */
    private String h2;
    /**
     * css h3 标签.
     */
    private String h3;
    /**
     * css h4 标签.
     */
    private String h4;
    /**
     * css h5 标签.
     */
    private String h5;
    /**
     * css 加粗 标签.
     */
    private String strong;
}
