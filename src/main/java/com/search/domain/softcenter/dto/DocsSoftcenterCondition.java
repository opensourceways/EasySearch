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
package com.search.domain.softcenter.dto;


import com.search.domain.base.dto.SearchDocsBaseCondition;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DocsSoftcenterCondition extends SearchDocsBaseCondition implements Cloneable {

    /**
     * 数据类型.
     */
    private String dataType;
    /**
     * 版本.
     */
    private String version;
    /**
     * os.
     */
    private String os;
    /**
     * arch.
     */
    private String arch;
    /**
     * category.
     */
    private String category;
    /**
     * 关键字类型.
     */
    private String keywordType;
    /**
     * 名字顺序.
     */
    private String nameOrder;
    /**
     * 欧拉 os 版本.
     */
    private String eulerOsVersion;

    @Override
    public DocsSoftcenterCondition clone() {
        DocsSoftcenterCondition condition = null;
        try {
            condition = (DocsSoftcenterCondition) super.clone();
        } catch (CloneNotSupportedException e) {

        }
        return condition;
    }

}
