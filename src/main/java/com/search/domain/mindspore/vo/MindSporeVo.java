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
package com.search.domain.mindspore.vo;

import com.search.domain.base.vo.CommunityBaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MindSporeVo extends CommunityBaseVo {
    /**
     * 档案.
     */
    private String archives;
    /**
     * 成分.
     */
    private String components;
    /**
     * 子类.
     */
    private String subclass;

    /**
     * 类型.
     */
    private String type;


    /**
     * 版本.
     */
    private String version;
}
