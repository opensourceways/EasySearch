package com.search.adapter.vo;

import com.search.domain.base.vo.CommunityBaseVo;
import com.search.domain.softcenter.vo.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SoftWareVo extends CommunityBaseVo {

    List<FieldApplicationVo> all;
    List<RPMPackageVo> rpmpkg;
    List<ApplicationPackageVo> apppkg;
    List<EPKGPackageVo> epkgpkg;
    List<ApplicationVersionVo> appversion;
    long total;

    public SoftWareVo() {
        this.all = new ArrayList<>();
        this.rpmpkg = new ArrayList<>();
        this.apppkg = new ArrayList<>();
        this.epkgpkg = new ArrayList<>();
        this.appversion = new ArrayList<>();
    }
}
