package com.search.docsearch.entity.software;


import com.search.docsearch.dto.software.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class SoftwareSearchResponce {
    List<SoftwareAllDto> all;
    List<SoftwareRpmDto> rpmpkg;
    List<SoftwareAppChildrenDto> apppkg;
    List<SoftwareEpkgDto> epkgpkg;
    List<SoftwareAppVersionDto> appversion;
    List<SoftwareOepkgDto> oepkg;
    long total;

    public SoftwareSearchResponce() {
        this.all = new ArrayList<>();
        this.rpmpkg = new ArrayList<>();
        this.apppkg = new ArrayList<>();
        this.epkgpkg = new ArrayList<>();
        this.appversion = new ArrayList<>();
    }
}
