package com.cmg.java.亿级流量架构书.test;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;

@Slf4j
public abstract class BaseTest<T> {

    @Autowired
    public WebApplicationContext webApplicationContext;
    public static MockMvc mockMvc;
    public HttpHeaders httpHeaders = new HttpHeaders();

    @Autowired
    public void init() {
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJ3ZWIiLCJpc3MiOiJmdXNlIiwiZXhwIjoxNjQwMTYwNjYxLCJpYXQiOjE2Mzk1NTIyNjEsImp0aSI6IjBkMmQ2MTM5ZjliMjQ5NjQ4MDJlZTcyZjhmMzBhN2Y4IiwidXNlcm5hbWUiOiIxODY4NDcxMTIxNiJ9.M72ErggEHwgWq_WuOVns7u5acOjuA-pRXbbcoD01TyU");
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterAll
    public static void clearMockMvc() {
        mockMvc = null;
    }

    public abstract T mockOne();

    public void printResponse(MvcResult mvcResult) throws UnsupportedEncodingException {
        MockHttpServletResponse mockHttpServletResponse = mvcResult.getResponse();
        log.info("status:" + mockHttpServletResponse.getStatus());
        log.info("response:" + mockHttpServletResponse.getContentAsString(Charset.forName("UTF-8")));
    }

    public void post(String url, Object object) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(JSONObject.toJSONString(object))
                .headers(httpHeaders)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.log()).andReturn();
        printResponse(mvcResult);
    }

    public void getWithPathVariable(String url, Object... objects) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url, objects)
                .headers(httpHeaders)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.log())
                .andReturn();
        printResponse(mvcResult);
    }

    public void getWithParams(String url, Map<String, String> map) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url);
        for (String key : map.keySet()) {
            builder.param(key, map.get(key));
        }
        MvcResult mvcResult = mockMvc.perform(builder
                .headers(httpHeaders)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.log())
                .andReturn();
        printResponse(mvcResult);
    }

    public void get(String url) throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url);
        MvcResult mvcResult = mockMvc.perform(builder
                .headers(httpHeaders)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.log())
                .andReturn();
        printResponse(mvcResult);
    }

    public void postWithPathVariable(String url, Object... objects) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url, objects)
                .headers(httpHeaders)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.log())
                .andReturn();
        printResponse(mvcResult);
    }
}
