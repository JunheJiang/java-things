package com.cmg.java.亿级流量架构书.test;

import com.cmg.java.亿级流量架构书.App;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AsyncTest extends BaseTest {

    @Override
    public Object mockOne() {
        Object contentPush = new Object();
        return contentPush;
    }

    @Test
    @DisplayName("三个任务并发异步")
    public void asyncArrange3ToFinal1() throws Exception {
        get("/asyncArrange3ToFinal1");
    }

    @Test
    @DisplayName("两个并发调用后处理结果")
    public void ehcacheHeapCache() throws Exception {
        get("/asyncArrange2FistThen1");
    }

    @Test
    @DisplayName("先执行后一个然后与并发执行两个的结果一并结算")
    public void ehcacheHeapOutsideCache() throws Exception {
        get("/asyncArrange1Then2Final1");
    }

    @Test
    @DisplayName("hystrix缓存请求")
    public void requestCache() throws Exception {
        get("/product/info");
    }

    @Test
    @DisplayName("客户端批次请求")
    public void clientBatchQuery() throws Exception {
        List<Long> list = Lists.newArrayList();
        for (int i = 0; i < 10000; i++) {
            list.add(1L);
        }
        //todo
        post("/product/clientBatchQuery", list);
    }

    @Test
    @DisplayName("合并请求")
    public void combineRequest() throws Exception {
        get("/product/combineRequest");
    }
}
