package com.search.docsearch.dto.software;

import lombok.Data;

@Data
public class SoftwareNameDocsDto {
    String name;
    String pkgId;

    public SoftwareNameDocsDto(String name,String pkgId){
        this.name=name;
        this.pkgId=pkgId;
    }
}