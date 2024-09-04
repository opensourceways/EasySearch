package com.search.infrastructure.search.openeuler.dataobject;

import lombok.Data;

@Data
public class OpenEulerDo {
    /**
     * 标题
     */
    private String title;
    /**
     * 档案
     */
    private String archives;
    /**
     * 文章名
     */
    private String articleName;
    /**
     * 日期
     */
    private String date;
    /**
     * es数据id,唯一
     */
    private String id;
    /**
     * 语言
     */
    private String lang;
    /**
     * 路径,用于前台跳转
     */
    private String path;
    /**
     * 文本内容
     */
    private String textContent;

    /**
     * 类型
     */
    private String type;
    /**
     * 版本
     */
    private String version;
    /**
     * 类别
     */
    private String category;
    /**
     * anchor
     */
    Boolean anchor;
    /**
     * 归档策略
     */
    private String archivePolicy;
    /**
     * 作者
     */
    private String author;
    /**
     * 横幅
     */
    private String banner;
    /**
     * 公司
     */
    private String company;
    /**
     * 欧拉论坛ID
     */
    private String eulerForumId;
    /**
     * 行业
     */
    private String industry;
    /**
     * 图片
     */
    private String img;
    /**
     * 第二标题,用于service
     */
    private String secondaryTitle;
    /**
     * 信号
     */
    private String sig;
    /**
     * 概括
     */
    private String summary;
    /**
     * 标题模板
     */
    private String titletemplate;

}