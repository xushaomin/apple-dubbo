package com.appleframework.dubbo.rpc.kafka;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Result;
import com.appleframework.dubbo.rpc.kafka.consumer.MessageConsumer;
import com.appleframework.dubbo.rpc.kafka.producer.MessageProducer;

public class KafkaQueueRequestor {
	
    private String topic;
    private MessageProducer producer;
    private MessageConsumer consumer;

    public KafkaQueueRequestor(KafkaClient kafkaClient, String topic) throws Exception {
        super();

        if(topic == null) {
            throw new Exception("Invalid topic");
        }
        
        setTopic(topic);
        setProducer(kafkaClient.getProducer());
        setConsumer(kafkaClient.getConsumer());
        
    }

    public Result request(Invocation invocation) throws Exception {
		getProducer().send(topic, invocation);
        return (Result)getConsumer().receive();
    }
    
    public Result request(Invocation invocation, long timeout) throws Exception {
		getProducer().send(topic, invocation);
        return (Result)getConsumer().receive(timeout);
    }

    public void close() throws Exception {
    	getProducer().close();
    	getConsumer().close();
    }

	public MessageProducer getProducer() {
		return producer;
	}

	public void setProducer(MessageProducer producer) {
		this.producer = producer;
	}

	public MessageConsumer getConsumer() {
		return consumer;
	}

	public void setConsumer(MessageConsumer consumer) {
		this.consumer = consumer;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
	
}