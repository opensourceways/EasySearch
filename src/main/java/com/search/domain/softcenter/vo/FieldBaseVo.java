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
public class FieldBaseVo {
    /**
     * 包名.
     */
    String name;
    /**
     * 版本.
     */
    String version;

    /**
     * 操作系统.
     */
    String os;
    /**
     * arch.
     */
    String arch;

    /**
     * 源码仓库.
     */
    String srcRepo;
    /**
     * 描述.
     */
    String description;

    /**
     * html页面.
     */
    String htmlurl;
    /**
     * 数据类型.
     */
    String dataType;
    /**
     * 数据id，源于数据库.
     */
    String id;
    /**
     * bin下载地址.
     */
    String binDownloadUrl;
    /**
     * 下载.
     */
    String installation;
    /**
     * pkgId.
     */
    String pkgId;
    /**
     * 类别.
     */
    String category;
}