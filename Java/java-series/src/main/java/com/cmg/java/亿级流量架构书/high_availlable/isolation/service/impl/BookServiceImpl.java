package com.cmg.java.亿级流量架构书.high_availlable.isolation.service.impl;

import com.cmg.java.亿级流量架构书.high_availlable.isolation.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    @Override
    public String getBook(String id) {
        return "book sucks..";
    }
}
