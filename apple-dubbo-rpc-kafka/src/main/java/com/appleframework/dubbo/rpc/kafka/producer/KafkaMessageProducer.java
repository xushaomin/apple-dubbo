package com.appleframework.dubbo.rpc.kafka.producer;

import com.appleframework.dubbo.rpc.kafka.utils.ByteUtils;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

public class KafkaMessageProducer implements MessageProducer {

	private Producer<String, byte[]> producer;

	@Override
	public void close() throws Exception {
		try {
			producer.close();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public void send(String topic, Object message) throws Exception {
		try {
			KeyedMessage<String, byte[]> producerData 
				= new KeyedMessage<String, byte[]>(topic, ByteUtils.toBytes(message));
				producer.send(producerData);
		} catch (Exception e) {
			throw new Exception(e);
		}
		
	}

	public Producer<String, byte[]> getProducer() {
		return producer;
	}

	public void setProducer(Producer<String, byte[]> producer) {
		this.producer = producer;
	}

}
