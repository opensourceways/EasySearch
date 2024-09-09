/* Copyright (c) 2024 openEuler Community
 EasySoftware is licensed under the Mulan PSL v2.
 You can use this software according to the terms and conditions of the Mulan PSL v2.
 You may obtain a copy of Mulan PSL v2 at:
     http://license.coscl.org.cn/MulanPSL2
 THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 See the Mulan PSL v2 for more details.
*/
package com.search.adapter.vo;

import com.search.domain.base.vo.CommunityBaseVo;
import com.search.domain.softcenter.vo.ApplicationPackageVo;
import com.search.domain.softcenter.vo.EPKGPackageVo;
import com.search.domain.softcenter.vo.FieldApplicationVo;
import com.search.domain.softcenter.vo.RPMPackageVo;
import com.search.domain.softcenter.vo.ApplicationVersionVo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SoftWareVo extends CommunityBaseVo {
    /**
     * the list of all data types.
     */
    private List<FieldApplicationVo> all;

    /**
     * the list of rpmpkg data types.
     */
    private List<RPMPackageVo> rpmpkg;

    /**
     * the list of apppkg data types.
     */
    private List<ApplicationPackageVo> apppkg;

    /**
     * the list of epkgpkg data types.
     */
    private List<EPKGPackageVo> epkgpkg;
    /**
     * the list of appversion data types.
     */
    private List<ApplicationVersionVo> appversion;

    /**
     * result count.
     */
    private long total;

    /**
     * 无参构造.
     */
    public SoftWareVo() {
        this.all = new ArrayList<>();
        this.rpmpkg = new ArrayList<>();
        this.apppkg = new ArrayList<>();
        this.epkgpkg = new ArrayList<>();
        this.appversion = new ArrayList<>();
    }
}
