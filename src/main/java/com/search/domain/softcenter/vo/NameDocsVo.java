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
public class NameDocsVo {
    /**
     * 包名.
     */
    private String name;
    /**
     * pkgId.
     */
    private String pkgId;
    /**
     * 版本.
     */
    private String version;

    /**
     * 有参构造，初始化.
     *
     * @param name    包名 .
     * @param pkgId   pkgId.
     * @param version 版本.
     */
    public NameDocsVo(String name, String pkgId, String version) {
        this.name = name;
        this.pkgId = pkgId;
        this.version = version;
    }
}
