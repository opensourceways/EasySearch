package com.search.docsearch.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SoftwarekeywordTypeEnum {
    /**
     * NAME serach.
     */
    NAME("name", "名称"),
    /**
     * DESCRIPTION serach.
     */
    DESCRIPTION("description", "描述"),
    /**
     * FILE serach.
     */
    FILE("file", "文件"),
    /**
     * SUMMARY serach.
     */
    SUMMARY("summary", "概要");

    /**
     * The type of search.
     */
    private final String keywordType;

    /**
     * return message.
     */
    private final String message;

    public static boolean isSupportKeywordType(String keywordType) {
        for (SoftwarekeywordTypeEnum value : SoftwarekeywordTypeEnum.values()) {
            if (value.getKeywordType().equals(keywordType))
                return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
