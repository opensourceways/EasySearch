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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.search.docsearch.multirecall.composite.Component;
import com.search.docsearch.multirecall.composite.DataComposite;
import com.search.docsearch.multirecall.composite.cdata.EsRecallData;
import com.search.docsearch.multirecall.composite.cdata.GRecallData;

import java.util.Collections;
@SpringBootTest
public class CompositeTest {
    
    /**
      * 测试: 多路召回结果获取
    */
    @Test
    void testGetChild() {
        DataComposite dataComposite = new DataComposite();
        Component mockComponent1 = new EsRecallData(Collections.emptyMap());
        Component mockComponent2 = new GRecallData(Collections.emptyMap());
        
        dataComposite.add(mockComponent1);
        dataComposite.add(mockComponent2);
        assertEquals(mockComponent1, dataComposite.getChild(0));
        assertEquals(mockComponent2, dataComposite.getChild(1));
    }

    /**
      * 测试: 多路召回结果获取下标越界
    */
    @Test
    void testGetChildWithError() {
        DataComposite dataComposite = new DataComposite();
        Component mockComponent1 = new EsRecallData(Collections.emptyMap());
        Component mockComponent2 = new GRecallData(Collections.emptyMap());
        
        dataComposite.add(mockComponent1);
        dataComposite.add(mockComponent2);

        IndexOutOfBoundsException exception = assertThrows(
            IndexOutOfBoundsException.class,
                () -> dataComposite.getChild(2));
        assertEquals("Index 2 out of bounds for length 2",exception.getMessage());        
    }

    /**
      * 测试: 多路召回列表错误操作
    */
    @Test
    void testGetReslistWithError() {
        DataComposite dataComposite = new DataComposite();
    
        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
                () -> dataComposite.getResList());
        assertEquals("composite do not have reslist",exception.getMessage());        
    }

    /**
      * 测试: 多路召回列表移除
    */
    @Test
    void testRemoveComponent() {
        DataComposite dataComposite = new DataComposite();
        Component mockComponent1 = new EsRecallData(Collections.emptyMap());
        Component mockComponent2 = new GRecallData(Collections.emptyMap());

        dataComposite.add(mockComponent1);
        dataComposite.add(mockComponent2);
        dataComposite.remove(mockComponent1);
        assertEquals(1, dataComposite.getSize());
    }

    /**
      * 测试: 多路召回结果粗筛异常测试
    */
    @Test
    void testFliteringRecallWithError() {
        DataComposite dataComposite = new DataComposite();
        Component mockComponent = new EsRecallData(Collections.emptyMap());
        Component errorComponent = new TestRecallData(Collections.emptyMap());
        dataComposite.add(mockComponent);
        dataComposite.add(errorComponent);

        RuntimeException exception = assertThrows(
            RuntimeException.class,
                () -> errorComponent.filter(null));

        assertEquals("error when process the recall res",exception.getMessage());    
    }

}
