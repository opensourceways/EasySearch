/* Copyright (c) 2024 openEuler Community
 EasySoftware is licensed under the Mulan PSL v2.
 You can use this software according to the terms and conditions of the Mulan PSL v2.
 You may obtain a copy of Mulan PSL v2 at:
     http://license.coscl.org.cn/MulanPSL2
 THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 See the Mulan PSL v2 for more details.
*/
package com.search.adapter.enums;

import com.search.common.constant.SourceConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SearchSourceEnum {
    /**
     * Enum datesource of openeuler .
     */
    OPENEULER(SourceConstant.SOURCE_OPENEULER, "欧拉"),
    /**
     * Enum datesource of opengauss .
     */
    OPENGAUSS(SourceConstant.SOURCE_OPENGAUSS, "高斯"),
    /**
     * Enum datesource of openmind .
     */
    OPENMIND(SourceConstant.SOURCE_OPENMIND, "openMind"),

    /**
     * Enum datesource of mindSpore .
     */
    MINDSPORE(SourceConstant.SOURCE_MINDSPORE, "mindSpore"),
    /**
     * Enum datesource of softcenter .
     */
    SOFTCENTER(SourceConstant.SOURCE_SOFTCENTER, "软件中心");
    private final String source;
    private final String info;

    /**
     * 根据传入的dataSource获取project的SearchSourceEnum
     *
     * @param dataSource 数据源 .
     * @return SearchSourceEnum.
     */
    public static SearchSourceEnum getRequestTypeByDatasource(String dataSource) {
        for (SearchSourceEnum value : SearchSourceEnum.values()) {
            if (value.getSource().equals(dataSource))
                return value;
        }
        return null;
    }
}
