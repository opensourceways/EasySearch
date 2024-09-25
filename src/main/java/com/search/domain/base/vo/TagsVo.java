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
package com.search.domain.base.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagsVo {
    /**
     * 数据大类.
     */
    private String key;
    /**
     * 文档数量.
     */
    private Long count;


    /**
     * 无参构造，初始化TagsVo.
     */
    public TagsVo() {

    }

    /**
     * 有参构造，初始化TagsVo.
     *
     * @param key   关键字 .
     * @param count 总量.
     */
    public TagsVo(String key, Long count) {
        this.key = key;
        this.count = count;
    }
}
