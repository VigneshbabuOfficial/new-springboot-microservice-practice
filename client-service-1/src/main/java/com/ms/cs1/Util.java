package com.ms.cs1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.reactive.function.client.WebClient;

public class Util extends Thread {
	
	private transient Logger logger = LogManager.getLogger(Util.class);

	public String requestId;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Util(String requestId) {
		super();
		this.requestId = requestId;
	}

	@Override
	public void run() {
		triggerBulHead();
	}

	private void triggerBulHead() {
		for (int i = 1; i <= 5; i++) {
			logger.info(i+" - /client-service-2/getMessageBH");
			WebClient.builder().build().get().uri("http://localhost:8082/client-service-2/getMessageBH")
					.header("source-id", requestId).retrieve().bodyToMono(String.class).block();
		}
	}

}
