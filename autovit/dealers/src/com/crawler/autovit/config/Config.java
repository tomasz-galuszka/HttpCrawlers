package com.crawler.autovit.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

	private final static String configFile = "config.properties";
	private Integer port;
	private String ip;
	private Boolean useProxy;
	private String type;

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Boolean getUseProxy() {
		return useProxy;
	}

	public void setUseProxy(Boolean useProxy) {
		this.useProxy = useProxy;
	}

	public Config() throws ConfigException {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			if (classLoader == null) {
				classLoader = Class.class.getClassLoader();
			}

			input = classLoader.getResourceAsStream(configFile);
			try {
				prop.load(input);
			} catch (IOException e) {
				throw new ConfigException("Brak pliku config.properties z konfigruacją w katalogu programu.", e);
			}
			port = Integer.valueOf(prop.getProperty("port"));
			useProxy = Boolean.valueOf(prop.getProperty("useProxy"));
			ip = prop.getProperty("ip");
			type = prop.getProperty("type");

		} catch (Exception ex) {
			throw new ConfigException("Niepoprawna konfiguracja. Sprawdź instrukcję i poprawnie skonfiguruj crawlera.", ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					throw new ConfigException("Błąd podczas zamykania pliku konfiguracyjnego.\n Uruchom program w konsoli i sprawdź logi.", e);
				}
			}
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
