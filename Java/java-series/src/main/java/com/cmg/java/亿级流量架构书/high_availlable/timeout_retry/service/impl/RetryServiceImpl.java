package com.cmg.java.亿级流量架构书.high_availlable.timeout_retry.service.impl;

import com.cmg.java.亿级流量架构书.high_availlable.timeout_retry.service.RetryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RetryServiceImpl implements RetryService {
    @Override
//    @Retryable()
    public void handleException() {

    }
}
