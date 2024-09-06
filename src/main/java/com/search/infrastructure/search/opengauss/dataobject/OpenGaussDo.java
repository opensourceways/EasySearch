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
package com.search.infrastructure.search.opengauss.dataobject;

import lombok.Data;

import java.util.Date;

@Data
public class OpenGaussDo {
    /**
     * 档案
     */
    String archives;

    /**
     * 文章名
     */
    String articleName;


    /**
     * 作者
     */
    private String author;

    /**
     * anchor
     */
    Boolean anchor;


    /**
     * 横幅
     */
    private String banner;

    /**
     * 类别
     */
    String category;


    /**
     * 公司
     */
    private String company;
    /**
     * 日期
     */
    Date date;

    /**
     * 细节
     */
    Boolean detail;

    /**
     * css h1 标签
     */
    private String h1;

    /**
     * css h2 标签
     */
    private String h2;

    /**
     * css h3 标签
     */
    private String h3;

    /**
     * css h4 标签
     */
    private String h4;

    /**
     * css h5 标签
     */
    private String h5;

    /**
     * css 加粗 标签
     */
    private String strong;

    /**
     * id
     */
    String id;

    /**
     * 图片
     */
    String img;

    /**
     * img_移动
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
     * 语言
     */
    private String lang;

    /**
     * 路径
     */
    private String path;

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
     * 内容
     */
    private String textContent;
    /**
     * times
     */
    private String times;

    /**
     * 文章时间
     */
    private String time;


    /**
     * 标题
     */
    private String title;


    /**
     * 类型
     */
    private String type;


    /**
     * 版本
     */
    private String version;
    /**
     * sumary
     */
    private String sumary;
}
