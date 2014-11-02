package com.coches.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;

import com.coches.MessageBoxExceptionHandler;
import com.coches.config.Config;
import com.coches.config.ConfigException;

public class Connector {

	private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:15.0) Gecko/20120427 Firefox/15.0a1";

	public static String baseUrl = "http://www.coches.net";
	/**
	 * first - province name np a_coruna second- pageNumber np 1
	 */
	public static String searchUrl = baseUrl + "/ocasion/%s/?pg=%d&or=-1&fi=Year";

	private Config config;

	public Document getDocument(String www) throws ConnectionException {
		Document document = null;
		try {
			System.out.println(www);
			String response = getResponseAsString(www);
			TagNode cleanedHtml = new HtmlCleaner().clean(response);
			document = new DomSerializer(new CleanerProperties()).createDOM(cleanedHtml);
		} catch (IOException e) {
			throw new ConnectionException("Blad podczas pobierania strony www", e);
		} catch (ParserConfigurationException e) {
			throw new ConnectionException("Blad podczas pobierania strony www", e);
		}
		return document;
	}

	private String getResponseAsString(String www) throws IOException, ConnectionException {
		BufferedReader bufferedIn = new BufferedReader(new InputStreamReader(connect(www), "UTF-8"));
		String line;
		StringBuilder response = new StringBuilder();
		while ((line = bufferedIn.readLine()) != null) {
			response.append(line);
		}
		bufferedIn.close();
		return response.toString();
	}

	private InputStream connect(String www) throws IOException, ConnectionException {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(www);
			if (getConfig().getUseProxy().equals(true)) {

				if (getConfig().getType().equalsIgnoreCase("SOCKS")) {
					Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(config.getIp(), config.getPort()));
					connection = (HttpURLConnection) url.openConnection(proxy);
				} else {
					Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config.getIp(), config.getPort()));
					connection = (HttpURLConnection) url.openConnection(proxy);
				}

			} else {
				connection = (HttpURLConnection) url.openConnection();
			}
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", userAgent);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

			int responseCode = connection.getResponseCode();
			if (responseCode != 200) {
				throw new IOException("Problem z polaczeniem kod odpowiedzi: " + responseCode);
			}
		} catch (MalformedURLException e) {
			throw new ConnectionException("Niepoprawny adres url, sprawdz czy adres: " + www + " istnieje", e);
		} catch (ConfigException e) {
			MessageBoxExceptionHandler.handle(e);
		}
		return connection.getInputStream();
	}

	private Config getConfig() throws ConfigException {
		if (config == null) {
			config = new Config();
		}
		return config;
	}
}
