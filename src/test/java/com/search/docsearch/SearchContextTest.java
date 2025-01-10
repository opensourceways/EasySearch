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

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.search.docsearch.entity.vo.SearchCondition;
import com.search.docsearch.multirecall.composite.DataComposite;
import com.search.docsearch.multirecall.recall.MultiSearchContext;
import com.search.docsearch.strategy.ErrorSearchStrategy;
import com.search.docsearch.strategy.TestSearchStrategy;


@SpringBootTest
public class SearchContextTest {

    /**
      * 测试: 多路召回数目测试
    */
    @Test
    void testGetMultiSearch() {
        MultiSearchContext testMultiContext = new MultiSearchContext();
        TestSearchStrategy testStrategy = new TestSearchStrategy();

        testMultiContext.setSearchStrategy(testStrategy);

        assertEquals(1, testMultiContext.getMultiRecalls());
    }

    /**
      * 测试: 多路召回执行异常测试 - 空值condition
    */
    @Test
    void testExecuteMultiSearchWithNull() {
        MultiSearchContext testMultiContext = new MultiSearchContext();
        TestSearchStrategy testStrategy = new TestSearchStrategy();
    
        testMultiContext.setSearchStrategy(testStrategy);
        DataComposite testRes = testMultiContext.executeMultiSearch(null);
        assertEquals(1, testRes.getSize());
    }

    /**
      * 测试: 多路召回执行异常测试 - 一路召回失败
    */
    @Test
    void testExecuteMultiSearchWithOneErrors() {
        MultiSearchContext testMultiContext = new MultiSearchContext();
        TestSearchStrategy noramlStrategy = new TestSearchStrategy();
        ErrorSearchStrategy errorStrategy = new ErrorSearchStrategy();
        SearchCondition testCond = new SearchCondition();
        testCond.setKeyword("openEuler");

        testMultiContext.setSearchStrategy(noramlStrategy);
        testMultiContext.setSearchStrategy(errorStrategy);

        DataComposite testRes = testMultiContext.executeMultiSearch(testCond);
        assertEquals(1, testRes.getSize());
    }

    /**
      * 测试: 多路召回执行异常测试 - 全部召回失败
    */
    @Test
    void testExecuteMultiSearchWithALLErrors() {
        MultiSearchContext testMultiContext = new MultiSearchContext();
        ErrorSearchStrategy errorStrategy1 = new ErrorSearchStrategy();
        ErrorSearchStrategy errorStrategy2 = new ErrorSearchStrategy();
        SearchCondition testCond = new SearchCondition();
        testCond.setKeyword("openEuler");

        testMultiContext.setSearchStrategy(errorStrategy1);
        testMultiContext.setSearchStrategy(errorStrategy2);

        DataComposite testRes = testMultiContext.executeMultiSearch(testCond);
        assertEquals(0, testRes.getSize());
    }

}
