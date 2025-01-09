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
package com.search.docsearch;


import java.util.Map;

import com.search.docsearch.multirecall.composite.Component;

public class TestRecallData implements Component{

    /**
     * search entities of es recall, 
     */
    private Map<String, Object> recallList;
 
    /**
     * constructor of esRecall data
     * 
     * @param recallList 
     */
    public TestRecallData(Map<String, Object> recallList) {
        this.recallList = recallList;
    }
    
    /**
     * set an ApplyHandleRecord entity createAt field value.
     *
     * @param criteria The ApplicationPackageDO entity  createAt field for set
     */
    @Override
    public void filter(String filterPolicy) throws RuntimeException {
        throw new RuntimeException("error when process the recall res");
    }

    /**
     * return the recall list to client
     * 
     */
    @Override
    public Map<String, Object> getResList(){
        //writing filter logic here
        return null;
    }
}
