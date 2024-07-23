package com.search.docsearch.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SoftwarekeywordTypeEnum {
    NAME("name", "名称"),
    DESCRIPTION("description", "描述"),
    FILE("file","文件"),
    UBUNTU("ubuntu","ubuntu"),
    SUMMARY("summary", "概要");
    private final String keywordType;
    private final String message;

    public static  boolean isSupportKeywordType(String keywordType) {
        for (SoftwarekeywordTypeEnum value : SoftwarekeywordTypeEnum.values()) {
            if (value.getKeywordType().equals(keywordType))
                return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
