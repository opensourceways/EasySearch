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
package com.search.infrastructure.search.softcenter.dataobject;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SoftCenterDo {
    /**
     * 类型
     */
    private String type;
    /**
     * EPKG
     */
    private String EPKG;
    /**
     * IMAGE
     */
    private String IMAGE;
    /**
     * RPM
     */
    private String RPM;
    /**
     * appVer
     */
    private String appVer;
    /**
     * arch
     */
    private String arch;
    /**
     * binDownloadUrl
     */
    private String binDownloadUrl;

    /**
     * category
     */
    private String category;
    /**
     * dataType
     */
    private String dataType;
    /**
     * date
     */
    private Date date;
    /**
     * description
     */
    private String description;
    /**
     * downloadCount
     */
    private String downloadCount;
    /**
     * epkgUpdate
     */
    private Date epkgUpdate;
    /**
     * epkg_name
     */
    private String epkg_name;
    /**
     * eulerHomepage
     */
    private String eulerHomepage;

    /**
     * eulerOsVersion
     */
    private String eulerOsVersion;
    /**
     * iconUrl
     */
    private String iconUrl;
    /**
     * id
     */
    private String id;
    /**
     * maintainers
     */
    private String maintainers;
    /**
     * installation
     */
    private String installation;
    /**
     * name
     */
    private String name;
    /**
     * openeulerVersion;
     */
    private String openeulerVersion;
    /**
     * originPkg
     */
    private String originPkg;
    /**
     * os
     */
    private String os;
    /**
     * pkgId
     */
    private String pkgId;
    /**
     * pkgIds
     */
    private String pkgIds;
    /**
     * provideName
     */
    private String provideName;
    /**
     * providesText
     */
    private String providesText;
    /**
     * requiresText
     */
    private String requiresText;
    /**
     * rpmSize
     */
    private String rpmSize;
    /**
     * rpmUpdate
     */
    private Long rpmUpdate;
    /**
     * rpmUpdateAt
     */
    private String rpmUpdateAt;
    /**
     * rpm_name
     */
    private String rpm_name;
    /**
     * size
     */
    private String size;

    /**
     * srcRepo
     */
    private String srcRepo;
    /**
     * status
     */
    private String status;
    /**
     * summary
     */
    private String summary;
    /**
     * tags
     */
    private String tags;
    /**
     * tagsText
     */
    private String tagsText;
    /**
     * upHomepage
     */
    private String upHomepage;

    /**
     * updatetime
     */
    private String updatetime;

    /**
     * upstreamVersion
     */
    private String upstreamVersion;
    /**
     * upstreamVersion
     */
    private String version;
}
