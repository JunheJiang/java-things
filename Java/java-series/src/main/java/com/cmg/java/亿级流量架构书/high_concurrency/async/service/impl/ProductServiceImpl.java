package com.cmg.java.亿级流量架构书.high_concurrency.async.service.impl;

import com.cmg.java.亿级流量架构书.high_concurrency.async.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Override
    public String getProduct(Long id) {
        return "商品详情信息。。。。";
    }
}
