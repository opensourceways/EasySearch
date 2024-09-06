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
package com.search.infrastructure.search.softcenter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;

@Getter
@AllArgsConstructor
public enum SoftwareTypeEnum {
    /**
     * 领域应用 数据类型.
     */
    ALL("", "all", "all", "领域应用"),
    /**
     * rpm软件包" 数据类型.
     */
    RPMPKG("RPM", "rpmpkg", "rpmpkg", "rpm软件包"),
    /**
     * 容器镜像 数据类型.
     */
    APPLICATION("IMAGE", "application", "apppkg", "容器镜像"),
    /**
     * openeuler软件包 数据类型.
     */
    EKPG("EPKG", "epkg", "epkgpkg", "openeuler软件包"),
    /**
     * 上游兼容应用全景 数据类型.
     */
    APPVERSION("", "appversion", "appversion", "上游兼容应用全景");
    /**
     * 标签.
     */
    private final String tag;

    /**
     * 类型 对应es中类型.
     */
    private final String type;
    /**
     * 前台传的数据类型.
     */
    private final String frontDeskType;
    /**
     * 信息.
     */
    private final String message;

    /**
     * 通过frontDeskType获取es中的数据type.
     *
     * @param frontDeskType frontDeskType.
     * @return String.
     */
    public static String getTypeByfrontDeskType(String frontDeskType) {
        String type = null;
        for (SoftwareTypeEnum value : SoftwareTypeEnum.values()) {
            if (value.getFrontDeskType().equals(frontDeskType)) {
                type = value.getType();
                break;
            }
        }
        return type;
    }


    /**
     * 通过es中的数据type获取frontDeskType.
     *
     * @param type es中的数据类型.
     * @return String.
     */
    public static String getFrontDeskTypeByType(String type) {
        String frontDeskType = null;
        for (SoftwareTypeEnum value : SoftwareTypeEnum.values()) {
            if (value.getType().equals(type)) {
                frontDeskType = value.getFrontDeskType();
                break;
            }
        }
        return frontDeskType;
    }
}
