package com.appleframework.dubbo.rpc.kafka.producer;

public interface MessageProducer {

    void close() throws Exception;

    void send(String topic, Object message) throws Exception;

}
