package com.search.common.vo;

import lombok.Data;

@Data
public class OpenEulerVo extends CommunityBaseVo {
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
