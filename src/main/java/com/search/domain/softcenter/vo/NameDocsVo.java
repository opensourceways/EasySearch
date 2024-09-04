package com.search.domain.softcenter.vo;

import lombok.Data;

@Data
public class NameDocsVo {
    String name;
    String pkgId;
    String version;

    public NameDocsVo(String name, String pkgId, String version){
        this.name=name;
        this.pkgId=pkgId;
        this.version=version;
    }
}