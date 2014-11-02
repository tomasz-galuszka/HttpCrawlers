package com.asfr;

public class Logger {

	private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Logger.class);

	public static void log(String msg) {
		logger.info(msg);
	}

}
