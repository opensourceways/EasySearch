package com.search.domain.mindspore.vo;

import com.search.domain.base.vo.CommunityBaseVo;
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
