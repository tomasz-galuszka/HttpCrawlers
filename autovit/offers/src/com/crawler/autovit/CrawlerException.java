package com.crawler.autovit;

public class CrawlerException extends Exception {

	private static final long serialVersionUID = -4285532697713094539L;

	public CrawlerException(String message, Throwable cause) {
		super(message, cause);
	}

	public CrawlerException(String message) {
		super(message);
	}

}
