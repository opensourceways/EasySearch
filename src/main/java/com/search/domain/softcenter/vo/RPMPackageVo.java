package com.search.domain.softcenter.vo;

import lombok.Data;

@Data
public class RPMPackageVo extends FieldBaseVo {
    String rpmUpdateAt;
    String rpmCategory;
    String rpmSize;
    String summary;
    String requires;

    String provides;
    String originPkg;
}
