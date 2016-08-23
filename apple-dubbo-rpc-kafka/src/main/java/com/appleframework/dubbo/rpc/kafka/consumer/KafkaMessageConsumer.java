package com.appleframework.dubbo.rpc.kafka.consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.appleframework.dubbo.rpc.kafka.listener.MessageListener;
import com.appleframework.dubbo.rpc.kafka.utils.ByteUtils;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class KafkaMessageConsumer implements MessageConsumer {
	
	private MessageListener listener;

	@Override
	public MessageListener getMessageListener() throws Exception {
		return listener;
	}

	@Override
	public void setMessageListener(MessageListener listener) throws Exception {
		this.listener = listener;
	}

	@Override
	public Object receive() throws Exception {
		return message;
	}

	@Override
	public Object receive(long timeout) throws Exception {
		return message;
	}

	@Override
	public Object receiveNoWait() throws Exception {
		return message;
	}

	@Override
	public void close() throws Exception {
		if(null != connector)
			connector.shutdown();
	}
	
	private ConsumerConfig consumerConfig;
	
	private String topic;
    
	private Integer partitionsNum;
	
	private ConsumerConnector connector;
	
	private Object message;
			
	protected void init() {
		
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		
		connector = Consumer.createJavaConsumerConnector(consumerConfig);
		
		String[] topics = topic.split(",");
		for (int i = 0; i < topics.length; i++) {
			topicCountMap.put(topics[i], partitionsNum);
		}

		Map<String, List<KafkaStream<byte[], byte[]>>> topicMessageStreams 
			= connector.createMessageStreams(topicCountMap);
		List<KafkaStream<byte[], byte[]>> streams = new ArrayList<KafkaStream<byte[], byte[]>>();
		for (int i = 0; i < topics.length; i++) {
			streams.addAll(topicMessageStreams.get(topics[i]));
		}
		
	    //	    
		final ExecutorService executor = Executors.newFixedThreadPool(partitionsNum * topics.length);
	    for (final KafkaStream<byte[], byte[]> stream : streams) {
	    	executor.submit(new Runnable() {
				public void run() {
                    ConsumerIterator<byte[], byte[]> it = stream.iterator();
					while (it.hasNext()) {
						byte[] message = it.next().message();
						Object object = ByteUtils.fromByte(message);
						setMessage(object);
						listener.onMessage(object);
					}
                }
            });
	    }
	    
	    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	    	public void run() {
	    		executor.shutdown();
	    	}
	    }));
	}	

	public void setConsumerConfig(ConsumerConfig consumerConfig) {
		this.consumerConfig = consumerConfig;
	}

	public void setTopic(String topic) {
		this.topic = topic.trim().replaceAll(" ", "");
	}

	public void setPartitionsNum(Integer partitionsNum) {
		this.partitionsNum = partitionsNum;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

}
