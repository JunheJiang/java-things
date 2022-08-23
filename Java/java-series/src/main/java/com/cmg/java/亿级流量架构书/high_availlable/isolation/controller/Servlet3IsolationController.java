package com.cmg.java.亿级流量架构书.high_availlable.isolation.controller;

import com.cmg.java.亿级流量架构书.high_availlable.isolation.component.OneLevelAsyncContext;
import com.cmg.java.亿级流量架构书.high_availlable.isolation.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 异步化请求异步化处理获取信息
 * 吞吐量大了但不会提升响应时间
 */
@RestController
@Slf4j
public class Servlet3IsolationController {

    @Autowired
    OneLevelAsyncContext oneLevelAsyncContext;
    @Autowired
    BookService bookService;

    @GetMapping("/asyncInfo/book")
    public void asyncInfo(HttpServletRequest request, @RequestParam String id) {
        oneLevelAsyncContext.submitFuture(request, () -> bookService.getBook(id));
    }

}
