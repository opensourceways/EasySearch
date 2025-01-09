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
import java.util.Map;

public interface Component {
 
    /**
     * roughly filter the recalled results
     * 
     * @param filterPolicy strategy for filtering recall results
     */
    void filter(String filterPolicy) throws RuntimeException;

    /**
     * return the recall list to client
     * 
     */
    Map<String, Object> getResList();
}