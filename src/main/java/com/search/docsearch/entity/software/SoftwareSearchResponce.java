package com.search.docsearch.entity.software;


import com.search.docsearch.dto.software.SoftwareAppChildrenDto;
import com.search.docsearch.dto.software.SoftwareAppDto;
import com.search.docsearch.dto.software.SoftwareEpkgDto;
import com.search.docsearch.dto.software.SoftwareRpmDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class SoftwareSearchResponce {
    List<SoftwareAppDto> all;
    List<SoftwareRpmDto> rpmpkg;
    List<SoftwareAppChildrenDto>  apppkg;
    List<SoftwareEpkgDto> epkgpkg;
    long total;

    public SoftwareSearchResponce() {
        this.all = new ArrayList<>();
        this.rpmpkg = new ArrayList<>();
        this.apppkg = new ArrayList<>();
        this.epkgpkg = new ArrayList<>();
    }
}
