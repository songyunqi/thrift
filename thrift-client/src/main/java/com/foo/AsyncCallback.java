package com.foo;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class AsyncCallback implements org.apache.thrift.async.AsyncMethodCallback<java.util.Map<String, String>> {

    private CountDownLatch latch;

    public AsyncCallback(CountDownLatch latch) {
        this.latch = latch;
    }

    public void onComplete(Map<String, String> map) {
        System.out.println("onComplete");
        System.out.println(map);
        latch.countDown();
    }

    public void onError(Exception e) {
        System.out.println("onError");
        System.out.println(e.getMessage());
        latch.countDown();
    }
}
