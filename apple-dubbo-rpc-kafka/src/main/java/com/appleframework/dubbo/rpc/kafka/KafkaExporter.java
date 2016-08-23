package com.appleframework.dubbo.rpc.kafka;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.dubbo.rpc.protocol.AbstractExporter;
import com.appleframework.dubbo.rpc.kafka.consumer.MessageConsumer;
import com.appleframework.dubbo.rpc.kafka.listener.MessageListener;
import com.appleframework.dubbo.rpc.kafka.producer.MessageProducer;

public class KafkaExporter<T> extends AbstractExporter<T> {

	// use the given connection and topic to subscribe invocation and execute for a result, 
	// then send the result back to replyTo topic
	public KafkaExporter(final Invoker<T> invoker,final KafkaClient kafkaClient, final String topic) {
		super(invoker);
		try {
			
			MessageConsumer consumer = kafkaClient.getConsumer();
			MessageListener listener = new MessageListener(){
				public void onMessage(Object message) {
					try {
						Object invocation = message;
						Result result = null;
						try{
							result = invoker.invoke((Invocation) invocation);
						} catch (RpcException e) {
							result = new RpcResult(e);
						}
						MessageProducer producer = kafkaClient.getProducer();
						producer.send(topic, result);
						producer.close();
					} catch (Exception e) {
						throw new RpcException(e);
					}
				}
			};
			
			consumer.setMessageListener(listener);
			
		} catch (Exception e) {
			throw new RpcException(e);
		}
	}

}
