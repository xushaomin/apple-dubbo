package com.appleframework.dubbo.rpc.kafka.listener;

public interface MessageListener {
    void onMessage(Object message);
}