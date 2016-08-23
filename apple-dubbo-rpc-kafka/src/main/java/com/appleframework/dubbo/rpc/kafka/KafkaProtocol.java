package com.appleframework.dubbo.rpc.kafka;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Exporter;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.protocol.AbstractProtocol;

public class KafkaProtocol extends AbstractProtocol {

	public static final String NAME = "kafka";
	private final static int DEFAULT_PORT = 9092;
	// private final static String DEFAULT_TOPIC = "DUBBO";

	// p:
	private String topic;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	private KafkaClient kafkaClient;

	public KafkaClient getKafkaClient() {
		return kafkaClient;
	}

	public void setKafkaClient(KafkaClient kafkaClient) {
		this.kafkaClient = kafkaClient;
	}
	
	public int getDefaultPort() {
		return DEFAULT_PORT;
	}

	public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
		return new KafkaExporter<T>(invoker, kafkaClient, topic);
	}

	public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
		return new KafkaInvoker<T>(type, url, kafkaClient, topic);
	}

}
