package com.ms.cs2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;


@RestController
@RequestMapping("/client-service-2")
public class Controller {

	@Autowired
	AppLogger logger;

	@Autowired
	private RequestId requestId;

	@GetMapping
	public String getData() {
		logger.info("------  CONTROLLER -  GETDATA  ------------------");
		return "CLIENT-SERVICE-2 IS UP !!!";
	}

	@GetMapping("/service-1")
	public String getDataFromService1() {
		logger.info("------  CONTROLLER -  GET_DATA_FROM_SERVICE1  ------------------");
//		return restTemplate.getForObject("http://localhost:8081/client-service-1/others", String.class);
		return WebClient.builder().build().get().uri("http://localhost:8081/client-service-1/others")
				.header("source-id", requestId.getId()).retrieve().bodyToMono(String.class).block();
	}

	@GetMapping("/others")
	public String getDataFromService2() {
		logger.info("------  CONTROLLER -  GET_DATA_FROM_SERVICE2  ------------------");
		return "CLIENT-SERVICE-2 IS INVOKED !!!";
	}
}
