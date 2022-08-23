package com.cmg.java.亿级流量架构书.high_availlable.isolation.component;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Component
public class OneLevelAsyncContext {

    private AsyncListener asyncListener;
    private ThreadPoolExecutor threadPoolExecutor;

    public void submitFuture(final HttpServletRequest request, final Callable<Object> task) {
        String uri = request.getRequestURI();
        Map<String, String[]> params = request.getParameterMap();
        final AsyncContext asyncContext = request.startAsync();
        asyncContext.getRequest().setAttribute("uri", uri);
        asyncContext.getRequest().setAttribute("params", params);
        asyncContext.setTimeout(1000);
        if (asyncListener != null) {
            asyncContext.addListener(asyncListener);
        }
        threadPoolExecutor.submit(new CanceledCallable(asyncContext) {

            @Override
            public Object call() throws Exception {
                Object o = task.call();
                if (o == null) {
                    callBack(asyncContext, o, uri, params);
                }
                if (o instanceof CompletableFuture) {
                    CompletableFuture<Object> future = (CompletableFuture<Object>) o;
                    future.thenAccept(resultObject -> callBack(asyncContext, resultObject, uri, params))
                            .exceptionally(e -> {
                                callBack(asyncContext, "", uri, params);
                                return null;
                            });
                } else if (o instanceof String) {
                    callBack(asyncContext, o, uri, params);
                }
                return null;
            }
        });
    }

    private void callBack(AsyncContext asyncContext, Object result, String uri, Map<String, String[]> params) {
        HttpServletResponse resp = (HttpServletResponse) asyncContext.getResponse();
        try {
            if (result instanceof String) {
                write(resp, (String) result);
            } else {
                write(resp, JSONObject.toJSONString(result));
            }
        } catch (Throwable e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error(e.getMessage());
        } finally {
            asyncContext.complete();
        }
    }

    private void write(HttpServletResponse resp, String result) throws IOException {
        log.info("response str:::" + result);
        ServletOutputStream outputStream = resp.getOutputStream();
        outputStream.write(result.getBytes("utf-8"));
    }

    @PostConstruct
    public void init() {
        String poolSize = "5-10";
        String[] poolSizes = poolSize.split("-");
        //初始线程池大小
        int corePoolSize = Integer.valueOf(poolSizes[0]);
        //最大线程池大小
        int maximumPoolSize = Integer.valueOf(poolSizes[1]);
        LinkedBlockingDeque queue = new LinkedBlockingDeque<Runnable>(10000);
        threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize,
                200, TimeUnit.SECONDS,
                queue);
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        threadPoolExecutor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                if (r instanceof CanceledCallable) {
                    CanceledCallable cc = ((CanceledCallable) r);
                    AsyncContext asyncContext = cc.getAsyncContext();
                    if (asyncContext != null) {
                        try {
                            String uri = (String) asyncContext.getRequest().getAttribute("uri");
                            Map params = (Map) asyncContext.getRequest().getAttribute("params");
                            log.info("async request rejected, uri : {}, params : {}", uri, JSONObject.toJSON(params));
                            HttpServletResponse resp = (HttpServletResponse) asyncContext.getResponse();
                            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        } finally {
                            asyncContext.complete();
                        }
                    }
                }
            }
        });

        if (asyncListener == null) {
            asyncListener = new AsyncListener() {
                @Override
                public void onComplete(AsyncEvent event) throws IOException {
                    //todo
                }

                @Override
                public void onTimeout(AsyncEvent event) throws IOException {
                    AsyncContext asyncContext = event.getAsyncContext();
                    try {
                        String uri = (String) asyncContext.getRequest().getAttribute("uri");
                        Map params = (Map) asyncContext.getRequest().getAttribute("params");
                        log.error("async request onTimeout, uri : {}, params : {}", uri, JSONObject.toJSON(params));
                        HttpServletResponse resp = (HttpServletResponse) asyncContext.getResponse();
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    } finally {
                        asyncContext.complete();
                    }
                }

                @Override
                public void onError(AsyncEvent event) throws IOException {
                    AsyncContext asyncContext = event.getAsyncContext();
                    try {
                        String uri = (String) asyncContext.getRequest().getAttribute("uri");
                        Map params = (Map) asyncContext.getRequest().getAttribute("params");
                        log.info("async request onError, uri : {}, params : {}", uri, JSONObject.toJSON(params));
                        HttpServletResponse resp = (HttpServletResponse) asyncContext.getResponse();
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    } finally {
                        asyncContext.complete();
                    }
                }

                @Override
                public void onStartAsync(AsyncEvent event) throws IOException {
                    log.info("onStartAsync" + event.getAsyncContext());
                }
            };
        }

    }
}
