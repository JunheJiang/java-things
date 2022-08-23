package com.cmg.java.亿级流量架构书.high_concurrency.async.service.impl;

import com.cmg.java.亿级流量架构书.high_concurrency.async.service.PriceService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PriceServiceImpl implements PriceService {
    @Override
    public List<Double> getPrices(List<Long> ids) {
        log.info("getPrices.....");
        return Lists.newArrayList(1.0, 233.9);
    }
}
