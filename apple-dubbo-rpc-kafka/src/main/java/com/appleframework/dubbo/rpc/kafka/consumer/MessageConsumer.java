package com.appleframework.dubbo.rpc.kafka.consumer;

import com.appleframework.dubbo.rpc.kafka.listener.MessageListener;

public interface MessageConsumer {
	
    MessageListener getMessageListener() throws Exception;

    void setMessageListener(MessageListener listener) throws Exception;

    Object receive() throws Exception;

    Object receive(long timeout) throws Exception;

    Object receiveNoWait() throws Exception;

    void close() throws Exception;
}