package com.search.docsearch.dto.software;

import lombok.Data;

@Data
public class SoftwareNameDocsDto {
    String name;
    String pkgId;
    String version;

    public SoftwareNameDocsDto(String name,String pkgId,String version){
        this.name=name;
        this.pkgId=pkgId;
        this.version=version;
    }
}