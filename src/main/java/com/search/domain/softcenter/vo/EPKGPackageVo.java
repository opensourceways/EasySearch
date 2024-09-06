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
    String epkgUpdateAt;
    /**
     * epkg Size.
     */
    String epkgSize;
    /**
     * 概要.
     */
    String summary;

    /**
     * epkg 大类.
     */
    String epkgCategory;
    /**
     * requires.
     */
    String requires;
    /**
     * provides.
     */
    String provides;
    /**
     * 原始包名.
     */
    String originPkg;
}
