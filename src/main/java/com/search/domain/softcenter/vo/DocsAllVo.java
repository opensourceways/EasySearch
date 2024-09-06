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
package com.search.domain.softcenter.vo;

import com.search.domain.base.vo.CountVo;
import lombok.Data;

import java.util.List;

@Data
public class DocsAllVo extends CountVo {
    /**
     * list of nameDocs.
     */
    List<NameDocsVo> nameDocs;

    /**
     * 有参构造，初始化DocsAllVo
     *
     * @param key      数据类型 .
     * @param docCount 该类型数据总数
     * @param nameDocs 该类型数据top3.
     * @return DocsAllVo.
     */
    public DocsAllVo(String key, Long docCount, List<NameDocsVo> nameDocs) {
        this.key = key;
        this.doc_count = docCount;
        this.nameDocs = nameDocs;
    }


}