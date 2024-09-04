package com.search.infrastructure.search.softcenter.dataobject;

import lombok.Data;

import java.util.Date;

@Data
public class SoftCenterDo {
    String type;

    String EPKG;

    String IMAGE;

    String RPM;

    String appVer;

    String arch;

    String binDownloadUrl;


    String category;

    String dataType;

    Date date;

    String description;

    String downloadCount;

    Date epkgUpdate;

    String epkg_name;

    String eulerHomepage;


    String eulerOsVersion;

    String iconUrl;

    String id;

    String installation;

    String name;

    String openeulerVersion;

    String originPkg;

    String os;

    String pkgId;

    String pkgIds;

    String provideName;

    String providesText;

    String requiresText;

    Date rpmUpdate;

    String rpm_name;

    String size;


    String srcRepo;

    String status;

    String summary;

    String tags;

    String tagsText;

    String upHomepage;


    String updatetime;


    String upstreamVersion;

    String version;
}
