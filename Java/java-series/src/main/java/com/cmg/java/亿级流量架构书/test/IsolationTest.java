package com.cmg.java.亿级流量架构书.test;

import com.cmg.java.亿级流量架构书.App;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IsolationTest extends BaseTest<Object> {

    @Override
    public Object mockOne() {
        Object contentPush = new Object();
        return contentPush;
    }

    @Test
    @DisplayName("获取库存")
    public void getStock() throws Exception {
        getWithPathVariable("/getStock/{id}", "123");
    }

    @Test
    @DisplayName("获取书籍信息")
    public void getBook() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("id", "123");
        getWithParams("/asyncInfo/book", map);
    }

}
