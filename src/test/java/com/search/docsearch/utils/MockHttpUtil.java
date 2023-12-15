package com.search.docsearch.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.MultiValueMap;

/**
 *
 */
@Slf4j
public class MockHttpUtil {

    private static String Authorization = "token";

    private static String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbnRlcnByaXNlR3VpZCI6IjY1MDY0MzExOTU2NTE5ODIzMzciLCJzdG9yZU5vIjoiMzE4NzI4OSIsImRldmljZUd1aWQiOiIyMTEyMTUxNzUzMjAwODQwMDA2IiwidXNlckd1aWQiOiI2ODYzNjkyNTc4MTc3NDE3MjE3IiwiaWF0IjoxNjQyNTc4NjExLCJpc3MiOiJIb2xkZXIuY29tIn0=.Osaj77+977+977WdSO+/vXk977+9Owzvv70sT++/ve+/ve+/ve+/vQLehXrvv73vv71+SO+/vUc=";


    private static String headerOfUserInfo = "userInfo";
    private static String userInfo = "{\"operSubjectGuid\": \"2008141005594470007\",\"enterpriseGuid\":" +
            " \"6506431195651982337\",\"enterpriseName\": \"xx\",\"enterpriseNo\": \"08801187\",\"storeGuid\":" +
            " \"6619160595813892096\",\"storeName\": \"xxx\",\"storeNo\": \"6148139\",\"userGuid\": " +
            "\"6653489337230950401\",\"account\": \"200003\",\"tel\": \"16000000000\",\"name\": \"lf\"}\n";


    /**
     * get
     */
    public static String get(String uri, MultiValueMap<String, String> params, MockMvc mockMvc) {
        try {
            MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get(uri);
            handleRequestHeader(getRequest);
            if (params != null) {
                getRequest.params(params);
            }
            MvcResult tradeResult = mockMvc.perform(getRequest).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
            MockHttpServletResponse response = tradeResult.getResponse();
            response.setCharacterEncoding("UTF-8");
            return response.getContentAsString();
        } catch (Exception e) {
            log.error("get error:{}", e.getMessage());
            return null;
        }
    }

    /**
     * post
     */
    public static String post(String uri, MultiValueMap<String, String> params, MockMvc mockMvc) {
        try {
            MockHttpServletRequestBuilder postDataRequest = MockMvcRequestBuilders.post(uri);
            handleRequestHeader(postDataRequest);
            if (params != null) {
                postDataRequest.params(params);
            }
            MvcResult tradeResult = mockMvc.perform(postDataRequest).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
            MockHttpServletResponse response = tradeResult.getResponse();
            response.setCharacterEncoding("UTF-8");
            return response.getContentAsString();
        } catch (Exception e) {
            log.error("post data error:{}", e.getMessage());
            return null;
        }
    }

    /**
     * post
     */
    public static String post(String uri, Object body, MockMvc mockMvc) {
        try {
            MockHttpServletRequestBuilder postBodyRequest = MockMvcRequestBuilders.post(uri)
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                    .content(JSON.toJSONString(body));
            handleRequestHeader(postBodyRequest);
            MvcResult tradeResult = mockMvc.perform(postBodyRequest).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
            MockHttpServletResponse response = tradeResult.getResponse();
            response.setCharacterEncoding("UTF-8");
            return response.getContentAsString();
        } catch (Exception e) {
            log.error("post body error:{}", e.getMessage());
            return null;
        }
    }

    /**
     * put
     */
    public static String put(String uri, MultiValueMap<String, String> params, MockMvc mockMvc) {
        try {
            MockHttpServletRequestBuilder putDataRequest = MockMvcRequestBuilders.put(uri);
            handleRequestHeader(putDataRequest);
            if (params != null) {
                putDataRequest.params(params);
            }
            MvcResult tradeResult = mockMvc.perform(putDataRequest).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
            MockHttpServletResponse response = tradeResult.getResponse();
            response.setCharacterEncoding("UTF-8");
            return response.getContentAsString();
        } catch (Exception e) {
            log.error("put data error:{}", e.getMessage());
            return null;
        }
    }

    /**
     * put
     */
    public static String put(String uri, Object body, MockMvc mockMvc) {
        try {
            MockHttpServletRequestBuilder putBodyRequest = MockMvcRequestBuilders.put(uri)
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                    .content(JSON.toJSONString(body));
            handleRequestHeader(putBodyRequest);
            MvcResult tradeResult = mockMvc.perform(putBodyRequest).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
            MockHttpServletResponse response = tradeResult.getResponse();
            response.setCharacterEncoding("UTF-8");
            return response.getContentAsString();
        } catch (Exception e) {
            log.error("put body error:{}", e.getMessage());
            return null;
        }
    }

    /**
     * delete
     */
    public static String delete(String uri, MockMvc mockMvc) {
        try {
            MockHttpServletRequestBuilder deleteRequest = MockMvcRequestBuilders.delete(uri);
            handleRequestHeader(deleteRequest);
            MvcResult tradeResult = mockMvc.perform(deleteRequest).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
            MockHttpServletResponse response = tradeResult.getResponse();
            response.setCharacterEncoding("UTF-8");
            return response.getContentAsString();
        } catch (Exception e) {
            log.error("delete error:{}", e.getMessage());
            return null;
        }
    }

    private static void  handleRequestHeader(MockHttpServletRequestBuilder  requestBuilder){
        requestBuilder.header(Authorization, token);
        requestBuilder.header(headerOfUserInfo, userInfo);
    }

}
