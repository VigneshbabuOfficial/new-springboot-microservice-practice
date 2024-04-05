package com.ms.cs2;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.annotation.RequestScope;

import jakarta.servlet.http.HttpServletRequest;

@EnableDiscoveryClient
@SpringBootApplication
public class ClientService2Application {

	public static void main(String[] args) {
		SpringApplication.run(ClientService2Application.class, args);
	}
	
	@Bean
	AppLogger logger() {
		return AppLogger.getLogger(" ##### ClientService2Application ##### ");
	}
	
	@Bean
	@RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
	RequestId requestId(HttpServletRequest requestServlet) {

		String sourceId = requestServlet.getHeader("source-id");

		String requestId = null;

		if (Objects.nonNull(sourceId)) {
			requestId = sourceId;
		} else {
			String string = Long.toString(Instant.now().toEpochMilli());
			UUID requestUUID = UUID.nameUUIDFromBytes(string.getBytes());
			requestId = requestUUID.toString();
		}

		return new RequestId(requestId);
	}

}
