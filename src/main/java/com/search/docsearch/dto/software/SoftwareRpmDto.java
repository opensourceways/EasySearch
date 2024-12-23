package com.search.docsearch.dto.software;

import lombok.Data;

@Data
public class SoftwareRpmDto extends SoftwareBaseDto {
    String rpmUpdateAt;
    String rpmCategory;
    String rpmSize;
    String summary;
    String requires;

    String provides;
    String originPkg;
    String subPath;
}
