package com.ms.cs1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;


@RestController
@RequestMapping("/client-service-1")
public class Controller {

	@Autowired
	AppLogger logger;

	@Autowired
	private RequestId requestId;

	@GetMapping
	public String getData() {
		logger.info("------  CONTROLLER -  GETDATA  ------------------");
		return "CLIENT-SERVICE-1 IS UP !!!";
	}

	@GetMapping("/service-2")
	public String getDataFromService2() {
		logger.info("------  CONTROLLER -  GET_DATA_FROM_SERVICE2  ------------------");
		return WebClient.builder().build().get().uri("http://localhost:8082/client-service-2/others")
				.header("source-id", requestId.getId()).retrieve().bodyToMono(String.class).block();
//		return restTemplate.getForObject("http://localhost:8082/client-service-2/others", String.class);
	}

	@GetMapping("/others")
	public String getDataFromService1() {
		logger.info("------  CONTROLLER -  GET_DATA_FROM_SERVICE1  ------------------");
		return "CLIENT-SERVICE-1 IS INVOKED !!!";
	}
}
