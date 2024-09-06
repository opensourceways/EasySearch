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
package com.search.domain.softcenter.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ApplicationPackageVo extends FieldBaseVo {
    /**
     * tags
     */
    List<String> tags;
    /**
     * iconUrl
     */
    String iconUrl;
    /**
     * category
     */
    String category;
    /**
     * pkgIds
     */
    ApplicationPkgIdsVo pkgIds;
    /**
     * appVer
     */
    String appVer;

    /**
     * 无参构造
     *
     * @return ApplicationPackageVo.
     */
    public ApplicationPackageVo() {
        ApplicationPkgIdsVo applicationPkgIdsVo = new ApplicationPkgIdsVo();
        this.pkgIds = applicationPkgIdsVo;
    }

    /**
     * ApplicationPkgIdsVo obj.
     */
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
