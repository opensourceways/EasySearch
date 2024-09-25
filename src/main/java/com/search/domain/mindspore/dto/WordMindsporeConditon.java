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
package com.search.domain.mindspore.dto;
import com.search.adapter.condition.WordConditon;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class WordMindsporeConditon {
    /**
     * 输入语言.
     */
    private String query;

    /**
     * 数据索引.
     */
    private String index;


    /**
     * 有参构造，初始化TagsMindsporeCondition.
     *
     * @param index        数据索引 .
     * @param wordConditon 前台请求封装条件.
     */
    public WordMindsporeConditon(WordConditon wordConditon, String index) {
        this.query = wordConditon.getQuery();
        this.index = index;
    }
}
