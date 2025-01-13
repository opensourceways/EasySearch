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
package com.search.docsearch.multirecall.recall;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.search.docsearch.entity.vo.SearchCondition;
import com.search.docsearch.multirecall.composite.DataComposite;
import com.search.docsearch.multirecall.composite.Component;

public class MultiSearchContext {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiSearchContext.class);
    

    /**
     * List to store the search starteys
     * 
     */
    private List<SearchStrategy> searchStrategys = new ArrayList<>();
    
    /**
     * set Search Strategy into contex
     * 
     * @param SearchCondition 
     */
    public void setSearchStrategy(SearchStrategy searchStrategy) {
        this.searchStrategys.add(searchStrategy);
    }

    /**
     * excute multirecall on same query condition
     * 
     * @param SearchCondition the search query of user 
     */
    public DataComposite executeMultiSearch(SearchCondition condition) {
        DataComposite res = new DataComposite();
        for(SearchStrategy searchStgy : searchStrategys){
            //do recall, the validtaion of condition should implement by concrte strategy
            Component recallRes = searchStgy.search(condition);
            //in case one way does't recall anything  
            if (recallRes != null && recallRes.getResList().size() > 0){
                res.add(recallRes);
            }
        }
        return res;
    }

    /**
     * return the current registerd recall strategys
     * 
     */
    public int getMultiRecalls() {
        return searchStrategys.size();
    }
}