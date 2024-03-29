package com.search.docsearch.dto.software;

import lombok.Data;

import java.util.List;

@Data
public class SoftwareAppChildrenDto extends SoftwareBaseDto{
    List<String> tags;
    String iconUrl;
    String category;
    SoftwarePkgIdsDto pkgIds;
}
