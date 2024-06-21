package com.search.common.vo;

import lombok.Data;

@Data
public class OpenMindVo extends CommunityCssVo {
    /**
     * 文档类型
     */
    private String docsType;
    /**
     * from
     */
    private String from;
    /**
     * specify
     */
    private String specify;
    /**
     * 时间
     */
    private String time;
}
