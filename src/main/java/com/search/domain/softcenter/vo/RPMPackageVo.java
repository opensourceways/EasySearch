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


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RPMPackageVo extends FieldBaseVo {
    /**
     * rpm 更新时间.
     */
    private String rpmUpdateAt;
    /**
     * rpm 类别.
     */
    private  String rpmCategory;
    /**
     * rpm 大小.
     */
    private  String rpmSize;
    /**
     * 概要.
     */
    private  String summary;
    /**
     * requires.
     */
    private  String requires;
    /**
     * provides.
     */
    private  String provides;
    /**
     * 原始包名.
     */
    private  String originPkg;
}
