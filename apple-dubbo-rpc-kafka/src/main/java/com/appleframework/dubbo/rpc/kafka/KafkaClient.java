package com.appleframework.dubbo.rpc.kafka;

import com.appleframework.dubbo.rpc.kafka.consumer.MessageConsumer;
import com.appleframework.dubbo.rpc.kafka.producer.MessageProducer;

public class KafkaClient {

	private MessageProducer producer;

	private MessageConsumer consumer;

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

}
