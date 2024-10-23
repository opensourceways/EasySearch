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
package com.search.domain.openmind.vo;

import com.search.domain.base.vo.CommunityBaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenMindVo extends CommunityBaseVo {
    private String docsType;

    private String strong;


    private String articleName;

    private String h1;
    private String h2;
    private String h3;
    private String h4;
    private String h5;
    private String type;
    private String version;
}
