package com.cmg.java.亿级流量架构书.high_availlable.isolation.component;

import lombok.Data;

import javax.servlet.AsyncContext;
import java.util.concurrent.*;

@Data
public class CanceledCallable implements Callable {
    private AsyncContext asyncContext;

    public CanceledCallable(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }

    @Override
    public Object call() throws Exception {
        return null;
    }
}
