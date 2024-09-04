package com.search.domain.softcenter.vo;

import lombok.Data;

@Data
public class EPKGPackageVo extends FieldBaseVo {
    String epkgUpdateAt;
    String epkgSize;
    String summary;


    String epkgCategory;
    String requires;

    String provides;
    String originPkg;
}
