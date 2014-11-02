package com.asnl.connection;

public class ConnectionException extends Exception {

	private static final long serialVersionUID = 801554440776983471L;

	public ConnectionException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ConnectionException(String message) {
		super(message);
	}
	
}
