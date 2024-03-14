package com.search.docsearch.dto.software;

import lombok.Data;

@Data
public class SoftwareEpkgDto extends  SoftwareBaseDto {
    String epkgUpdateAt;
    String epkgSize;
    String summary;


    String epkgCategory;
    String requires;

    String provides;
    String originPkg;
}
