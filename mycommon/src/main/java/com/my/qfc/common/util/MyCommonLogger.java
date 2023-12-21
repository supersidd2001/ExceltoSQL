package com.my.qfc.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyCommonLogger {

	private static final Logger logger = LoggerFactory.getLogger(MyCommonLogger.class);

	public void performSomeAction() {
		logger.debug("Debug message");
		logger.info("Info message");
		logger.warn("Warning message");
		logger.error("Error message");
	}
}
