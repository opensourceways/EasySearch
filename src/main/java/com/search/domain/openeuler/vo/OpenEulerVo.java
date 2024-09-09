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
package com.search.domain.openeuler.vo;

import com.search.domain.base.vo.CommunityBaseVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OpenEulerVo extends CommunityBaseVo {
    /**
     * anchor.
     */
    private Boolean anchor;
    /**
     * 归档策略.
     */
    private String archivePolicy;
    /**
     * 作者.
     */
    private List author;
    /**
     * 横幅.
     */
    private String banner;
    /**
     * 公司.
     */
    private String company;
    /**
     * 欧拉论坛ID.
     */
    private String eulerForumId;
    /**
     * 行业.
     */
    private String industry;
    /**
     * 图片.
     */
    private String img;
    /**
     * 第二标题,用于service.
     */
    private String secondaryTitle;
    /**
     * 信号.
     */
    private String sig;
    /**
     * 概括.
     */
    private String summary;
    /**
     * 标题模板.
     */
    private String titletemplate;

}
