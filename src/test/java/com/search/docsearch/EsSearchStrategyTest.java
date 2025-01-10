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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.*;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.search.docsearch.config.EsfunctionScoreConfig;
import com.search.docsearch.entity.vo.SearchCondition;
import com.search.docsearch.multirecall.composite.Component;
import com.search.docsearch.multirecall.composite.cdata.EsRecallData;
import com.search.docsearch.utils.Trie;
import com.search.docsearch.multirecall.recall.cstrategy.EsSearchStrategy;

@ExtendWith(MockitoExtension.class)
public class EsSearchStrategyTest {

    /**
     * the es client
     * 
     */
    @Mock
    private RestHighLevelClient restHighLevelClient;

    /**
     * algorithim toolkits
     * 
     */
    @Mock
    private Trie trie;

    /**
     * the socre config
     * 
     */
    @Mock
    private EsfunctionScoreConfig esfunctionScoreConfig;

    /**
     * es search strategy
     * 
     */
    @InjectMocks
    private EsSearchStrategy esSearchStrategy;

    /**
     * search condition from user query
     * 
     */
    private SearchCondition searchCondition;

    /**
     * intialize mock obejct
     * 
     */
    @BeforeEach
    void setUp() {
        searchCondition = new SearchCondition();
        searchCondition.setKeyword("test");
        searchCondition.setLang("en");
        searchCondition.setPage(1);
        searchCondition.setPageSize(10);

        esSearchStrategy = new EsSearchStrategy(restHighLevelClient, "test_index", trie, esfunctionScoreConfig);
    }

    /**
      * 测试: es正常召回
    */
    @Test
    void testSearchWithSuccess() throws IOException {
        // Mock the search response
        SearchResponse searchResponse = mock(SearchResponse.class);
        SearchHits searchHits = mock(SearchHits.class);
        SearchHit searchHit = mock(SearchHit.class);

        Map<String, Object> mutableMap = new HashMap<>();
        mutableMap.put("textContent", "This is a test text.");
        mutableMap.put("title", "Test Title");
        when(searchResponse.getHits()).thenReturn(searchHits);
        when(searchHits.getHits()).thenReturn(Collections.singletonList(searchHit).toArray(new SearchHit[0]));
        when(searchHit.getSourceAsMap()).thenReturn(mutableMap);
        when(searchHit.getScore()).thenReturn(1.0f);
    
        when(restHighLevelClient.search(any(SearchRequest.class), eq(RequestOptions.DEFAULT))).thenReturn(searchResponse);

        // Mock the trie behavior
        when(trie.getWordSimilarityWithTopSearch(anyString(), anyInt())).thenReturn(1.0);

        // Perform the search
        Component result = esSearchStrategy.search(searchCondition);

        // Validate the result
        assertNotNull(result);
        assertTrue(result instanceof EsRecallData);
        EsRecallData recallData = (EsRecallData) result;
        Map<String, Object> data = recallData.getResList();
        assertNotNull(data); 
        List<Map<String, Object>> records = (List<Map<String, Object>>) data.get("records");
        assertNotNull(records);
        assertEquals(1, records.size());
        assertEquals("This is a test text.", records.get(0).get("textContent"));
        assertEquals("Test Title", records.get(0).get("title"));
    }

    /**
      * 测试: es 0召回测试
    */
    @Test
    void testSearchWithNoResults() throws IOException {
        // Mock the search response with no hits
        SearchResponse searchResponse = mock(SearchResponse.class);
        SearchHits searchHits = mock(SearchHits.class);
        SearchHit[] emptyArray = new SearchHit[0];

        when(searchResponse.getHits()).thenReturn(searchHits);
        when(searchHits.getHits()).thenReturn(emptyArray);
        when(restHighLevelClient.search(any(SearchRequest.class), eq(RequestOptions.DEFAULT))).thenReturn(searchResponse);

        // Perform the search
        Component result = esSearchStrategy.search(searchCondition);

        // Validate the result
        assertNotNull(result);
        assertTrue(result instanceof EsRecallData);
        EsRecallData recallData = (EsRecallData) result;
        Map<String, Object> data = recallData.getResList();
        assertNotNull(data);
    }

    /**
      * 测试: es连接错误测试
    */
    @Test
    void testSearchWithException() throws IOException {
        // Mock the search to throw an IOException
        when(restHighLevelClient.search(any(SearchRequest.class), eq(RequestOptions.DEFAULT))).thenThrow(new IOException("Test Exception"));

        // Perform the search
        Component result = esSearchStrategy.search(searchCondition);

        // Validate the result
        assertNotNull(result);
        // Validate the type
        assertTrue(result instanceof EsRecallData);
        EsRecallData recallData = (EsRecallData) result;
        Map<String, Object> data = recallData.getResList();
        assertNotNull(data);
    }
}