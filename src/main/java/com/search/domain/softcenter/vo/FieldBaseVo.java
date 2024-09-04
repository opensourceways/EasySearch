package com.search.domain.softcenter.vo;

import lombok.Data;

@Data
public class FieldBaseVo {
    String name;
    String version;


    String os;
    String arch;



    String srcRepo;

    String description;



    String htmlurl;
    String dataType;
    String id;

    String binDownloadUrl;

    String installation;
    String pkgId;

    String category;
}