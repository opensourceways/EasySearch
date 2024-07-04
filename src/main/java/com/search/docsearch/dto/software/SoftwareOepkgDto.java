package com.search.docsearch.dto.software;

import lombok.Data;

@Data
public class SoftwareOepkgDto {
    private String name;
    private String id;
    private String version;
    private String os;
    private String arch;
    private String category;
    private String rpmUpdateAt;
    private String srcRepo;
    private String rpmSize;
    private String binDownloadUrl;
    private String pkgId;
    private String subPath;
    private String license;
}
