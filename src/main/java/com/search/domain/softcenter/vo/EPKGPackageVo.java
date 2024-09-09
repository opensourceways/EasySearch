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
package com.search.domain.softcenter.vo;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class EPKGPackageVo extends FieldBaseVo {
    /**
     * epkg 更新时间.
     */
    private String epkgUpdateAt;
    /**
     * epkg Size.
     */
    private String epkgSize;
    /**
     * 概要.
     */
    private String summary;

    /**
     * epkg 大类.
     */
    private String epkgCategory;
    /**
     * requires.
     */
    private String requires;
    /**
     * provides.
     */
    private String provides;
    /**
     * 原始包名.
     */
    private String originPkg;
}
