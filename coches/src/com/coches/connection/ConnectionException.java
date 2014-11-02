package com.coches.connection;

public class ConnectionException extends Exception {

	private static final long serialVersionUID = -5833786583096935654L;

	public ConnectionException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ConnectionException(String message) {
		super(message);
	}
	
}
