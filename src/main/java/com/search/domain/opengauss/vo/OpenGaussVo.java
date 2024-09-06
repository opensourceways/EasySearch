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
package com.search.domain.opengauss.vo;

import com.search.domain.base.vo.CommunityCssVo;
import lombok.Data;

@Data
public class OpenGaussVo extends CommunityCssVo {
    /**
     * 作者
     */
    private String author;

    /**
     * banner
     */
    private String banner;
    /**
     * company
     */
    private String company;
    /**
     * detail
     */
    private String detail;

    /**
     * 图片
     */
    private String img;
    /**
     * img_mobile
     */
    private String img_mobile;
    /**
     * 行业
     */
    private String industry;
    /**
     * 标签
     */
    private String label;
    /**
     * 标签2
     */
    private String label2;
    /**
     * link url
     */
    private String link;
    /**
     * 位置
     */
    private String location;
    /**
     * 官方路径
     */
    private String officialpath;
    /**
     * 概括
     */
    private String summary;
    /**
     * tag
     */
    private String tag;

    /**
     * tags
     */
    private String tags;
    /**
     * times
     */
    private String times;
    /**
     * 文章时间
     */
    private String time;
}
