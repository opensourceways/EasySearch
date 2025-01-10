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
package com.search.docsearch.multirecall.composite;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class DataComposite implements Component {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Component.class);
    
    /**
     * Recall results list.
     */
    private List<Component> children = new ArrayList<>();
 
    /**
     * roughly filter the recalled results
     * 
     * @param filterPolicy strategy for filtering recall results
     */
    @Override
    public void filter(String filterPolicy) {
        for (Component child : children) {
            try {
                child.filter(filterPolicy);
            } catch (RuntimeException e) {
                LOGGER.error("catch unexcepted error when flitering the data");
            }
        }
    }

    /**
     * return the recall list to client
     * 
     */
    @Override
    public Map<String, Object> getResList(){
        throw new UnsupportedOperationException("composite do not have reslist");
    }

    /**
     * add component into DataComposite
     * 
     * @param component strategy for filtering recall results
     */
    public void add(Component component) {
        children.add(component);
    }

    /**
     * remove component from DataComposite
     * 
     * @param component strategy for filtering recall results
     */
    public void remove(Component component) {
        children.remove(component);
    }

     /**
     * return the idx to client
     * 
     * @param idx strategy for filtering recall results
     */
    public Component getChild(int idx) {
        return children.get(idx);
    }

    /**
     * return the size of res list
     * 
     */
    public int getSize(){
        return children.size();
    }

    /**
     * merge the other recall results into one way, based one the index 0 of children
     * 
     */
    public Map<String, Object> mergeResult(){
        if (children.size() <= 1) return this.getChild(0).getResList();

        Component a = children.get(0);
        Component b = children.get(1);

        Map<String, Object> ares = a.getResList();
        Map<String, Object> bres = b.getResList();

        List<Map<String, Object>> aresList = (List<Map<String, Object>>) ares.get("records");
        List<Map<String, Object>> bresList = (List<Map<String, Object>>) bres.get("records");
        int[] positions = {1, 3, 5, 7, 8};
        
        for (int pos : positions) {
            if (pos >= 0 && pos <= aresList.size() && pos < bresList.size()) {
                aresList.add(pos, bresList.get(pos));
            }
        }

        ares.put("records", aresList);
        return ares;
    }
}