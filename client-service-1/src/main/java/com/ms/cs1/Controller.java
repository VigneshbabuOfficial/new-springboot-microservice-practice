package com.ms.cs1;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;

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

	@GetMapping("/getMessageBH")
	@io.github.resilience4j.bulkhead.annotation.Bulkhead(name = "getMessageBH", fallbackMethod = "getMessageBHFallBack")
	public ResponseEntity<String> getMessageBH(@RequestParam(value = "name", defaultValue = "Hello") String name) {
		logger.info("----------- getMessageBH() call starts here ---------- ");
		return ResponseEntity.ok().body("Message from getMessageBH() :" + name);
	}

	public ResponseEntity<String> getMessageBHFallBack(RequestNotPermitted exception) {
		logger.info("Bulkhead has applied, So no further calls are getting accepted");
		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
				.body("Too many requests : No further request will be accepted. Plese try after sometime");
	}

	@GetMapping("/service-2/bh")
	public String getDataFromService2TestBH() {
		logger.info("------  CONTROLLER -  getDataFromService2TestBH  ------------------");

		Util util = new Util(requestId.getId());
		util.start();

		return WebClient.builder().build().get().uri("http://localhost:8082/client-service-2/getMessageBH")
				.header("source-id", requestId.getId()).retrieve().bodyToMono(String.class).block();
	}
}
