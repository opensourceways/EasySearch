package com.search.docsearch.dto.software;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SoftwareAllDto extends SoftwareBaseDto {
    List tags;
    Map pkgIds;
    String iconUrl;
}
