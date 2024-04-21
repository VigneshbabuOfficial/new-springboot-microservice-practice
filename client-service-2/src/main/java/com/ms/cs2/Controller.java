package com.ms.cs2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;


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
	
	@GetMapping("/getMessageBH")
    @Bulkhead(name = "getMessageBH", fallbackMethod = "getMessageBHFallBack")
    public ResponseEntity<String> getMessageBH(@RequestParam(value="name", defaultValue = "Hello") String name){
    	logger.info("----------- getMessageBH() call starts here ---------- ");
       return ResponseEntity.ok().body("Message from client-service-2 - getMessageBH() :" +name);
    }

    public ResponseEntity<String> getMessageBHFallBack(RequestNotPermitted exception) {
       logger.info("Bulkhead has applied, So no further calls are getting accepted");
       return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
      .body("Too many requests : No further request will be accepted. Plese try after sometime");
    }
}
