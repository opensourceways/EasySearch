package com.search.common.vo;

import lombok.Data;

@Data
public class MindSporeVo extends CommunityBaseVo {
    /**
     * 内容
     */
    private String components;
    /**
     * 子类别
     */
    private String subclass;


}
