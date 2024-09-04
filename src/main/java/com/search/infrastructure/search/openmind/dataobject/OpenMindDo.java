package com.search.infrastructure.search.openmind.dataobject;

import lombok.Data;

@Data
public class OpenMindDo {


    /**
     * 档案
     */
    private String archives;
    /**
     * 文章名
     */
    private String articleName;

    /**
     * 类别
     */
    private String category;


    /**
     * 日期
     */
    private String date;

    /**
     * 文档类型
     */
    private String docsType;

    /**
     * from
     */
    String from;


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
     * 标题
     */
    private String title;


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
     * 指定
     */
    private String specify;


    /**
     * css 加粗 标签
     */
    private String strong;

    /**
     * time
     */
    private String time;


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
     * 设置
     */
    private String settings;

}