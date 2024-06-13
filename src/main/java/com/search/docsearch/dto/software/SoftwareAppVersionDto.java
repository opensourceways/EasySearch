package com.search.docsearch.dto.software;

import lombok.Data;

@Data
public class SoftwareAppVersionDto {
    private String name;
    private String upHomepage;
    private String eulerHomepage;
    private String backend;
    private String upstreamVersion;
    private String openeulerVersion;
    private String ciVersion;
    private String status;
    private String id;
    private String eulerOsVersion;
}
