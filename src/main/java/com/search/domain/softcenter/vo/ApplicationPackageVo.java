package com.search.domain.softcenter.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ApplicationPackageVo extends FieldBaseVo {
    List<String> tags;
    String iconUrl;
    String category;
    ApplicationPkgIdsVo pkgIds;
    String appVer;

    public ApplicationPackageVo() {
        ApplicationPkgIdsVo applicationPkgIdsVo = new ApplicationPkgIdsVo();
        this.pkgIds = applicationPkgIdsVo;
    }

    @Data
    public class ApplicationPkgIdsVo {
        @JsonProperty("IMAGE")
        String IMAGE;
        @JsonProperty("EPKG")
        String EPKG;
        @JsonProperty("RPM")
        String RPM;
    }
}
