package com.ms.cs2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class AppLogger {

	private transient Logger logger = LogManager.getLogger(ClientService2Application.class);

	@Autowired
	private RequestId requestId;

	public static AppLogger getLogger(String loggerName) {

		AppLogger appLogger = new AppLogger();

		appLogger.logger = LogManager.getLogger(loggerName);

		return appLogger;
	}

	public void info(String message) {
		logger.info("message = " + message + ", requestId = " + requestId.getId());
	}

	public void error(String message) {
		logger.error("message = " + message + ", requestId = " + requestId.getId());
	}

	public void error(String message, Exception ex) {
		logger.error("message = " + message + ", exception = " + ex + ", requestId = " + requestId.getId());
	}

	public void debug(String message) {
		logger.debug("message = " + message + ", requestId = " + requestId.getId());
	}


}
