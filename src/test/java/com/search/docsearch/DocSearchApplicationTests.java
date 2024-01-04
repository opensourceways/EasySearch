package com.search.docsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.search.docsearch.entity.vo.SearchCondition;
import com.search.docsearch.entity.vo.SysResult;
import com.search.docsearch.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DocSearchApplication.class)
@WebAppConfiguration
@ContextConfiguration
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class DocSearchApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private ApplicationContext ap;

    private static final String REQUEST_MAPPING = "/search";

    @Autowired
    private SearchService searchService;

    @Before
    public void setupMockMvc() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }


    @Test
    public void test_sort() throws Exception {
        HashMap<String, Object> condition = new HashMap<>();
        condition.put("category", "news");
        condition.put("lang", "zh");
        condition.put("page", 1);
        condition.put("pageSize", 9);
        SysResult sysResult = post(REQUEST_MAPPING + "/sort", condition, mockMvc);
        assertThat(sysResult, notNullValue());
        assertThat(sysResult.getStatus(), is(200));
        assertThat(sysResult.getObj(), notNullValue());
        assertThat(sysResult.getObj(), instanceOf(Map.class));
        assertThat(new ArrayList<Object>(((Map) sysResult.getObj()).keySet()), everyItem(instanceOf(String.class)));
        assertThat(((Map<Object, Object>) sysResult.getObj()), allOf(hasKey("records"), hasKey("count"), hasKey("pageSize"), hasKey("page")));

        assertThat(((Map<Object, Object>) sysResult.getObj()).get("records"), allOf(notNullValue(), instanceOf(List.class)));
        List<Map<Object, Object>> records = (List) ((Map<String, Object>) sysResult.getObj()).get("records");
        assertThat(records, hasSize((int) condition.get("pageSize")));
        assertThat(records, allOf(everyItem(
                instanceOf(Map.class)),
                everyItem(hasKey("date")),
                everyItem(hasKey("summary")),
                everyItem(hasKey("archives")),
                everyItem(hasKey("articleName")),
                everyItem(hasKey("author")),
                everyItem(hasKey("banner")),
                everyItem(hasKey("textContent")),
                everyItem(hasKey("type")),
                everyItem(hasKey("title")),
                everyItem(hasKey("tags")),
                everyItem(hasKey("path")),
                everyItem(hasEntry("category", String.valueOf(condition.get("category")))),
                everyItem(hasEntry("lang", String.valueOf(condition.get("lang"))))
        ));
    }


    @Test
    public void test_docs() throws Exception {
        SearchCondition condition = new SearchCondition();
        condition.setKeyword("docker");
        condition.setLang("zh");
        condition.setPageSize(6);
        condition.setPage(1);
        List<Map<String, String>> limit = new ArrayList<>();
        condition.setLimit(limit);
        condition.setType("");
        Map<String, String> limitMap = new HashMap<>();
        limitMap.put("components", "MindSpore");
        limitMap.put("version", "r2.2");
        limit.add(limitMap);

        SysResult sysResult = post(REQUEST_MAPPING + "/docs", condition, mockMvc);
        assertThat(sysResult, notNullValue());
        assertThat(sysResult.getStatus(), is(200));
        assertThat(sysResult.getObj(), notNullValue());
        assertThat(sysResult.getObj(), instanceOf(Map.class));
        assertThat(new ArrayList<Object>(((Map) sysResult.getObj()).keySet()), everyItem(instanceOf(String.class)));
        Map<String, Object> obj = (Map<String, Object>) sysResult.getObj();
        assertThat(obj, allOf(hasKey("records"), hasKey("pageSize"), hasKey("page"), hasEntry("keyword", condition.getKeyword())));
        assertThat(obj.get("records"), allOf(notNullValue(), instanceOf(List.class)));
        assertThat((List<Object>) ((Map<Object, Object>) sysResult.getObj()).get("records"), everyItem(instanceOf(Map.class)));
        List<Map> records = (List) ((Map<Object, Object>) sysResult.getObj()).get("records");

        assertThat(records, hasSize(condition.getPageSize()));
        ArrayList<Map<String, String>> strRecord = new ArrayList<>();
        records.stream().forEach(r -> {
            r.replaceAll((k, v) -> String.valueOf(v).toLowerCase(Locale.ROOT));
            strRecord.add(r);
        });
        assertThat(strRecord, allOf(
                everyItem(instanceOf(Map.class)),
                everyItem(hasKey("path")),
                everyItem(hasKey("textContent")),
                everyItem(hasKey("type")),
                everyItem(hasKey("title")),
                everyItem(hasEntry("lang", String.valueOf(condition.getLang()))),
                everyItem(
                        anyOf(hasValue(containsString(condition.getKeyword().toLowerCase(Locale.ROOT))))
                )
        ));
    }


    @Test
    public void test_sugg() throws Exception {
        SearchCondition condition = new SearchCondition();
        condition.setKeyword("docker");
        condition.setLang("zh");
        condition.setPageSize(12);
        List<Map<String, String>> limit = new ArrayList<>();
        condition.setLimit(limit);
        condition.setType("");
        Map<String, String> limitMap = new HashMap<>();
        limitMap.put("type", "docs");
        limitMap.put("version", "23.09");
        SysResult sysResult = post(REQUEST_MAPPING + "/sugg", condition, mockMvc);
        assertThat(sysResult, notNullValue());
        assertThat(sysResult.getStatus(), is(200));
        assertThat(sysResult.getObj(), notNullValue());
        assertThat(sysResult.getObj(), instanceOf(Map.class));
        assertThat(new ArrayList<Object>(((Map) sysResult.getObj()).keySet()), everyItem(instanceOf(String.class)));
        Map<String, Object> obj = (Map<String, Object>) sysResult.getObj();
        assertThat(obj, hasKey("suggestList"));
        assertThat(obj.get("suggestList"), allOf(notNullValue(), instanceOf(List.class)));
        assertThat((List<Object>) ((Map<Object, Object>) sysResult.getObj()).get("suggestList"), everyItem(instanceOf(String.class)));
        List<String> suggestList = (List) ((Map<Object, Object>) sysResult.getObj()).get("suggestList");
        assertThat(suggestList, hasSize(condition.getPageSize()));
        for (int i = 0; i < suggestList.size(); i++) {
            suggestList.set(i, suggestList.get(i).toLowerCase(Locale.ROOT));
        }
        assertThat(suggestList, anyOf(everyItem(containsString(condition.getKeyword().toLowerCase(Locale.ROOT)))));
    }


    @Test
    public void test_count() throws Exception {
        SearchCondition condition = new SearchCondition();
        condition.setKeyword("迁移");
        condition.setLang("zh");
        List<Map<String, String>> limit = new ArrayList<>();
        condition.setLimit(limit);
        Map<String, String> limitMap = new HashMap<>();
        limit.add(limitMap);
        limitMap.put("type", "docs");
        limitMap.put("version", "23.09");
        SysResult sysResult = post(REQUEST_MAPPING + "/count", condition, mockMvc);
        assertThat(sysResult, notNullValue());
        assertThat(sysResult.getStatus(), is(200));
        assertThat(sysResult.getObj(), notNullValue());
        assertThat(sysResult.getObj(), instanceOf(Map.class));
        assertThat(new ArrayList<Object>(((Map) sysResult.getObj()).keySet()), everyItem(instanceOf(String.class)));
        Map<String, Object> obj = (Map<String, Object>) sysResult.getObj();
        assertThat(obj, allOf(hasKey("total")));
        assertThat(obj.get("total"), allOf(notNullValue(), instanceOf(List.class)));
        assertThat((List<Object>) ((Map<Object, Object>) sysResult.getObj()).get("total"), everyItem(instanceOf(Map.class)));
        List<Map<String, Object>> total = (List) ((Map<Object, Object>) sysResult.getObj()).get("total");
        assertThat(total, hasSize(condition.getPageSize()));
        assertThat(total, allOf(
                everyItem(instanceOf(Map.class)),
                everyItem(hasKey("doc_count")),
                everyItem(hasKey("key"))
        ));
    }

    private  SysResult post(String uri, Object body, MockMvc mockMvc) {
        try {
            MockHttpServletRequestBuilder postBodyRequest = MockMvcRequestBuilders.post(uri)
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                    .content(JSON.toJSONString(body));
            MvcResult tradeResult = mockMvc.perform(postBodyRequest).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
            MockHttpServletResponse response = tradeResult.getResponse();
            response.setCharacterEncoding("UTF-8");
            String content = response.getContentAsString();
            response.getContentAsString();
            assertThat(content, not(isEmptyOrNullString()));
            return JSONObject.parseObject(content, SysResult.class);
        } catch (Exception e) {
            log.error("post body error:{}", e.getMessage());
            return null;
        }
    }
}
