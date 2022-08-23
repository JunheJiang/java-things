package com.cmg.java.亿级流量架构书.high_concurrency.async.service;

import java.util.List;

public interface PriceService {
    List<Double> getPrices(List<Long> ids);
}
