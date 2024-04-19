package com.search.docsearch.dto.software;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SoftwarePkgIdsDto {
    @JsonProperty("IMAGE")
    String IMAGE;
    @JsonProperty("EPKG")
    String EPKG;
    @JsonProperty("RPM")
    String RPM;
}
