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
package com.search.docsearch.strategy;

import java.util.Collections;

import com.search.docsearch.entity.vo.SearchCondition;
import com.search.docsearch.multirecall.composite.Component;
import com.search.docsearch.multirecall.composite.cdata.EsRecallData;
import com.search.docsearch.multirecall.recall.SearchStrategy;

public class TestSearchStrategy implements SearchStrategy {
    /**
     * roughly filter the recalled results
     * 
     * @param SearchCondition paraClient mannage by spring aoc
     */
    @Override
    public Component search(SearchCondition condition) {
        //writing google search logic here 
        return new EsRecallData(Collections.emptyMap());
    }
}