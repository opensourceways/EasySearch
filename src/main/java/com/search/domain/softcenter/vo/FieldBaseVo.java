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
public class FieldBaseVo {
    /**
     * 包名.
     */
    private String name;
    /**
     * 版本.
     */
    private String version;

    /**
     * 操作系统.
     */
    private String os;
    /**
     * arch.
     */
    private String arch;

    /**
     * 源码仓库.
     */
    private String srcRepo;
    /**
     * 描述.
     */
    private String description;

    /**
     * html页面.
     */
    private String htmlurl;
    /**
     * 数据类型.
     */
    private String dataType;
    /**
     * 数据id，源于数据库.
     */
    private String id;
    /**
     * bin下载地址.
     */
    private String binDownloadUrl;
    /**
     * 下载.
     */
    private String installation;
    /**
     * pkgId.
     */
    private String pkgId;
    /**
     * 类别.
     */
    private String category;
}
