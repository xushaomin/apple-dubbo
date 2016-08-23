package com.appleframework.dubbo.rpc.kafka;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.dubbo.rpc.protocol.AbstractInvoker;

public class KafkaInvoker<T> extends AbstractInvoker<T> {

	KafkaClient kafkaClient = null;
	String topic = null;
	int timeout = 1000;
	
	public KafkaInvoker(Class<T> type, URL url, KafkaClient kafkaClient, String topic) {
		super(type, url);
		this.kafkaClient = kafkaClient;
		this.topic = topic;
		timeout = url.getParameter(Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT);
	}
	
	@Override
	protected Result doInvoke(Invocation invocation) throws Throwable {
		try {
			// transfer a RPC to a kafka-requestor and return the call result
			KafkaQueueRequestor requestor = new KafkaQueueRequestor(kafkaClient, topic);
			Result result = requestor.request(invocation, timeout);
			if(result == null)
				return new RpcResult(new RpcException("request is timeout in " + timeout + "ms"));
			return result;
		} catch (Exception e) {
			throw new RpcException(e);
		}
	}

}
