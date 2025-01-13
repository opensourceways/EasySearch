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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import com.search.docsearch.entity.vo.SearchCondition;
import com.search.docsearch.multirecall.composite.Component;
import com.search.docsearch.multirecall.composite.cdata.GRecallData;
import com.search.docsearch.multirecall.recall.cstrategy.GSearchStrategy;
import com.search.docsearch.properties.GoogleSearchProperties;
@ExtendWith(MockitoExtension.class)
public class GSearchStrategyTests {
    
    /**
     * insert google serach properties
     */
    @Mock
    private GoogleSearchProperties gProperties;

    /**
     * the search service
     */
    @InjectMocks
    private GSearchStrategy gSearchStrategy;

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

        gSearchStrategy = new GSearchStrategy(gProperties);
    }
    /**
     * 测试：获得google搜索结果
     * @throws IOException 
     * 
     */
    @Test
    public void testGoogleSearchApi() throws IOException {
        
        // Mock the http connection 
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        when(mockConnection.getRequestMethod()).thenReturn("GET");
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        String mockResponse = "{\"items\":[{\"title\":\"openeuler开源社区\",\"link\":\"http://euler.com\",\"snippet\":\"openeuler提供了一系列...\"}]}";
        when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream(mockResponse.getBytes(StandardCharsets.UTF_8)));

        // Perform the search
        Component result = gSearchStrategy.search(searchCondition);

        // 验证结果
        assertEquals("test", (String) ((Map<String, Object>) result.getResList()).get("keyword"));
        List<Map<String, Object>> records = (List<Map<String, Object>>) result.getResList().get("records");
        assertEquals(1, records.size());
        assertEquals("openeuler开源社区", records.get(0).get("title"));
        assertEquals("http://euler.com", records.get(0).get("path"));
        assertEquals("openeuler提供了一系列...", records.get(0).get("textContent"));
        assertEquals("en", records.get(0).get("lang"));
 
        verify(mockConnection).setRequestMethod("GET");
        verify(mockConnection).getResponseCode();
        verify(mockConnection).getInputStream();
    }

    /**
     * 测试: 获得google搜索结果异常
     * 
     * @throws IOException
     */
      @Test
    void testGetGoogleSearchApiWithError() throws IOException {

        // Mock the http connection
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);

        // Perform the search
        Component result = gSearchStrategy.search(searchCondition);

        // 验证结果
        assertEquals(null, (String) ((Map<String, Object>) result.getResList()).get("keyword"));
        List<Map<String, Object>> records = (List<Map<String, Object>>) result.getResList().get("records");
        assertEquals(null, records);

        // Validate the result
        assertNotNull(result);
        // Validate the type
        assertTrue(result instanceof GRecallData);
        GRecallData recallData = (GRecallData) result;
        Map<String, Object> data = recallData.getResList();
        assertNotNull(data);
    }
}