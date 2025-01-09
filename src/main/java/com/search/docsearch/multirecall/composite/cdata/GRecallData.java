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
package com.search.docsearch.multirecall.composite.cdata;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.search.docsearch.multirecall.composite.Component;

public class GRecallData implements Component{
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GRecallData.class);
    
    /**
     * search entities of es recall, 
     */
    private Map<String, Object> recallList;
 
    /**
     * constructor of esRecall data
     * 
     * @param recallList 
     */
    public GRecallData(Map<String, Object> recallList) {
        this.recallList = recallList;
    }
    
    /**
     * set an ApplyHandleRecord entity createAt field value.
     *
     * @param criteria The ApplicationPackageDO entity  createAt field for set
     */
    @Override
    public void filter(String filterPolicy) throws RuntimeException {
        //writing filter logic here
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
