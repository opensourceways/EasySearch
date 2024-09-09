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
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApplicationPackageVo extends FieldBaseVo {
    /**
     * tags.
     */
    private List<String> tags;
    /**
     * iconUrl.
     */
    private String iconUrl;
    /**
     * category.
     */
    private String category;
    /**
     * pkgIds.
     */
    private ApplicationPkgIdsVo pkgIds;
    /**
     * appVer.
     */
    private String appVer;

    /**
     * 无参构造.
     */
    public ApplicationPackageVo() {
        ApplicationPkgIdsVo applicationPkgIdsVo = new ApplicationPkgIdsVo();
        this.pkgIds = applicationPkgIdsVo;
    }

    /**
     * ApplicationPkgIdsVo obj.
     */
    @Getter
    @Setter
    public static class ApplicationPkgIdsVo {
        /**
         * image pkgid.
         */
        @JsonProperty("IMAGE")
        private String IMAGE;
        /**
         * epkg pkgid.
         */
        @JsonProperty("EPKG")
        private String EPKG;
        /**
         * RPM pkgid.
         */
        @JsonProperty("RPM")
        private String RPM;
    }
}
