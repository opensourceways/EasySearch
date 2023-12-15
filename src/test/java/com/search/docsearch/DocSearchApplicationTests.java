package com.search.docsearch;

import com.alibaba.fastjson.JSON;
import com.search.docsearch.entity.vo.SearchCondition;
import com.search.docsearch.utils.JsonFileUtil;
import com.search.docsearch.utils.MockHttpUtil;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DocSearchApplication.class)
@WebAppConfiguration
@ContextConfiguration
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DocSearchApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private ApplicationContext ap;

    private static final String REQUEST_MAPPING = "/search";

    private static String adjustOrderGuid = "";


    @Before
    public void setupMockMvc() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    /**
     * sort接口
     */
    @Test
    public void testSort() throws Exception {
        Map<String, String> condition = JSON.parseObject(JsonFileUtil.read("request/sort.json"), Map.class);
        String content = MockHttpUtil.post(REQUEST_MAPPING + "/sort", condition, mockMvc);
        System.out.println("sort   content ："+content);
        adjustOrderGuid = content;
    }


    /**
     * docs接口
     */
    @Test
    public void testDocs() throws Exception {
        SearchCondition condition = JSON.parseObject(JsonFileUtil.read("request/docs.json"), SearchCondition.class);
        String content = MockHttpUtil.post(REQUEST_MAPPING + "/docs", condition, mockMvc);
        System.out.println("docs content ："+content);
        adjustOrderGuid = content;
    }



    /**
     * getSuggestion接口
     */
    @Test
    public void testGetSuggestion() throws Exception {
        SearchCondition condition = JSON.parseObject(JsonFileUtil.read("request/sugg.json"), SearchCondition.class);
        String content = MockHttpUtil.post(REQUEST_MAPPING + "/sugg", condition, mockMvc);
        System.out.println("getSuggestion   content ："+content);
        adjustOrderGuid = content;
    }


    /**
     * count接口
     */
    @Test
    public void testCount() throws Exception {
        SearchCondition condition = JSON.parseObject(JsonFileUtil.read("request/count.json"), SearchCondition.class);
        String content = MockHttpUtil.post(REQUEST_MAPPING + "/count", condition, mockMvc);
        System.out.println("count   content ："+content);
        adjustOrderGuid = content;
    }


}
