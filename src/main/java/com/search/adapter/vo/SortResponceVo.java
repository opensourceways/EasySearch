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
package com.search.adapter.vo;

import com.search.domain.base.vo.CommunityBaseVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SortResponceVo<T extends CommunityBaseVo> {
    /**
     * list of object.
     */
    private List<T> records;
    /**
     * current request pageSize.
     */
    private Integer pageSize;

    /**
     * current request page.
     */
    private Integer page;
    /**
     * result count.
     */
    private Long count;

    /**
     * 有参构造.
     *
     * @param records  数据主题 .
     * @param pageSize 每页数量.
     * @param page     页码.
     * @param count    命中数据总量.
     */
    public SortResponceVo(List<T> records, Integer pageSize, Integer page, Long count) {
        this.records = records;
        this.pageSize = pageSize;
        this.page = page;
        this.count = count;
    }
}
