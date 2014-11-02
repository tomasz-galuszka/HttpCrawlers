package com.crawler.otomoto.general.connection;

public class OtoMotoConnectionException extends Exception {

	private static final long serialVersionUID = -5833786583096935654L;

	public OtoMotoConnectionException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public OtoMotoConnectionException(String message) {
		super(message);
	}
	
}
