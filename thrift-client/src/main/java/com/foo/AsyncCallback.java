package com.foo;

import java.util.Map;

public class AsyncCallback implements org.apache.thrift.async.AsyncMethodCallback<java.util.Map<String, String>> {

    public void onComplete(Map<String, String> map) {
        System.out.println("onComplete");
        System.out.println(map);
    }

    public void onError(Exception e) {
        System.out.println("onError");
    }
}
