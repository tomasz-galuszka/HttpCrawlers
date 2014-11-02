package com.crawler.autowereld.data;

public class DataException extends Exception {

	private static final long serialVersionUID = 1L;

	public DataException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataException(String message) {
		super(message);
	}

	public DataException(Throwable cause) {
		super(cause);
	}

}
