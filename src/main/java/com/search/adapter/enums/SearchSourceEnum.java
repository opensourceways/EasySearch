package com.search.adapter.enums;

import com.search.common.constant.SourceConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SearchSourceEnum {

    OPENEULER(SourceConstant.SOURCE_OPENEULER, "欧拉"),
    OPENGAUSS(SourceConstant.SOURCE_OPENGAUSS, "高斯"),
    OPENMIND(SourceConstant.SOURCE_OPENMIND, "openMind"),
    MINDSPORE(SourceConstant.SOURCE_MINDSPORE, "mindSpore"),
    SOFTCENTER(SourceConstant.SOURCE_SOFTCENTER, "软件中心");
    private final String source;
    private final String info;

    public static SearchSourceEnum getRequestTypeByDatasource(String dataSource) {
        for (SearchSourceEnum value : SearchSourceEnum.values()) {
            if (value.getSource().equals(dataSource))
                return value;
        }
        return null;
    }
}
