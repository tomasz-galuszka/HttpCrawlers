package com.crawler.autovit.data;

public class DataException extends Exception {

	private static final long serialVersionUID = 1L;

	public DataException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataException(String message) {
		super(message);
	}

}