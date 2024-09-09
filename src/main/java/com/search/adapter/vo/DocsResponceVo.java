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
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DocsResponceVo<T extends CommunityBaseVo> {
    /**
     * list of object.
     */
    List<T> records;
    /**
     * current request pageSize.
     */
    Integer pageSize;

    /**
     * current request page.
     */
    Integer page;

    /**
     * current request keyword.
     */
    String keyword;
    /**
     * result count.
     */
    long total;

    public DocsResponceVo(List<T> records, Integer pageSize, Integer page, String keyword) {
        this.records = records;
        this.pageSize = pageSize;
        this.page = page;
        this.keyword = keyword;
    }
}
