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

import lombok.Data;
@Data
public class ApplicationVersionVo {
    /**
     * name.
     */
    private String name;
    /**
     * upHomepage.
     */
    private String upHomepage;
    /**
     * eulerHomepage.
     */
    private String eulerHomepage;
    /**
     * backend.
     */
    private String backend;
    /**
     * upstreamVersion.
     */
    private String upstreamVersion;
    /**
     * openeulerVersion.
     */
    private String openeulerVersion;
    /**
     * ciVersion.
     */
    private String ciVersion;
    /**
     * status.
     */
    private String status;
    /**
     * id.
     */
    private String id;
    /**
     * eulerOsVersion.
     */
    private String eulerOsVersion;
}