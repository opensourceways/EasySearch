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
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.search.docsearch.multirecall.composite.Component;
import com.search.docsearch.multirecall.composite.DataComposite;
import com.search.docsearch.multirecall.composite.cdata.EsRecallData;
import com.search.docsearch.multirecall.composite.cdata.GRecallData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    /**
      * 测试: normlize加权融合测试
    */
    @Test
    void testWeightedMerge() {
        // 设置mockComponent1的返回数据
        List<Map<String, Object>> records1 = new ArrayList<>();
        Map<String, Object> record1_1 = new HashMap<>();
        record1_1.put("score", 3.0);
        records1.add(record1_1);

        Map<String, Object> record1_2 = new HashMap<>();
        record1_2.put("score", 1.0);
        records1.add(record1_2);
 
        // 设置mockComponent2的返回数据
        List<Map<String, Object>> records2 = new ArrayList<>();
        Map<String, Object> record2_1 = new HashMap<>();
        record2_1.put("score", 2.5);
        records2.add(record2_1);
 
        Map<String, Object> record2_2 = new HashMap<>();
        record2_2.put("score", 4.0);
        records2.add(record2_2);

        DataComposite dataComposite = new DataComposite();

        Component mockComponent1 =  new EsRecallData(Collections.singletonMap("records", records1));
        Component mockComponent2 =  new EsRecallData(Collections.singletonMap("records", records2));
    
        dataComposite.add(mockComponent1);
        dataComposite.add(mockComponent2);
        // 校验是否按pagesize返回正确个数
        int pageSize = 3;
        List<Map<String, Object>> result = dataComposite.weightedMerge(pageSize);
        assertEquals(pageSize, result.size());
 
        // 验证结果是否按分数降序排列
        for (int i = 0; i < result.size() - 1; i++) {
            double score1 = (Double) result.get(i).get("score");
            double score2 = (Double) result.get(i + 1).get("score");
            assertTrue(score1 >= score2, "Results should be sorted in descending order by score");
        }
    }


}
