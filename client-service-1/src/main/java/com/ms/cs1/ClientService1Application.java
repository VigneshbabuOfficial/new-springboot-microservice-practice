package com.ms.cs1;

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
public class ClientService1Application {

	public static void main(String[] args) {
		SpringApplication.run(ClientService1Application.class, args);
	}

	@Bean
	AppLogger logger() {
		return AppLogger.getLogger(" ##### ClientService1Application ##### ");
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
