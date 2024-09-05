package com.search.domain.base.vo;

import lombok.Data;

@Data
public class CommunityBaseVo {
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
}