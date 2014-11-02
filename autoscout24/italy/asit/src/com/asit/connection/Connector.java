package com.asit.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;

public class Connector {

	private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:15.0) Gecko/20120427 Firefox/15.0a1";
	private String baseUrl = "http://veicoli.autoscout24.it";
	private String carDetailsBaseUrl = "http://www.autoscout24.it";

	private String baseQuery = "/?atype=C&cy=I&ustate=N,U&sort=mileage&desc=1&results=80&page=%d";
	private String topArticlesQuery = "/ArticleList/GetTopArticles?atype=C&cy=I&ustate=N,U&sort=mileage&desc=1&results=80&page=%d";
	private String carDetailsQueryStandard = "/Details.aspx?id=%s&asrc=st";
	private String carDetailsQueryPremium = "/Details.aspx?id=%s&asrc=pl";

	public Document getSearchPage(int page) throws ConnectionException {
		Document document = null;
		try {
			String requestUrl = baseUrl + String.format(baseQuery, page);
			System.out.println(requestUrl);
			String response = getResponseAsString(requestUrl);
			TagNode cleanedHtml = new HtmlCleaner().clean(response);
			document = new DomSerializer(new CleanerProperties()).createDOM(cleanedHtml);
		} catch (IOException e) {
			handleException(e);
		} catch (ParserConfigurationException e) {
			handleException(e);
		}
		return document;
	}

	public String getPremiumCarsPage(int page) throws ConnectionException {
		String requestAddress = baseUrl + String.format(topArticlesQuery, page);
		System.out.println(requestAddress);
		String response = "";
		try {
			response = getResponseAsString(requestAddress);
		} catch (IOException e) {
			throw new ConnectionException(e.getMessage());
		}
		return response;
	}

	public Document getPremiumCarDetailsPage(String id) throws ConnectionException {
		return getCarDetailsPage(id, true);
	}

	public Document getNormalCarDetailsPage(String id) throws ConnectionException {
		return getCarDetailsPage(id, false);
	}

	private Document getCarDetailsPage(String id, boolean isPremium) throws ConnectionException {
		Document document = null;
		String requestAddress = carDetailsBaseUrl;
		if (isPremium) {
			requestAddress += String.format(carDetailsQueryPremium, id);
		} else {
			requestAddress += String.format(carDetailsQueryStandard, id);
		}
		System.out.println(requestAddress);
		try {
			String response = getResponseAsString(requestAddress);
			TagNode cleanedHtml = new HtmlCleaner().clean(response);
			document = new DomSerializer(new CleanerProperties()).createDOM(cleanedHtml);
		} catch (IOException e) {
			handleException(e);
		} catch (ParserConfigurationException e) {
			handleException(e);
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
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", userAgent);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

			int responseCode = connection.getResponseCode();
			if (responseCode != 200) {
				throw new IOException("Problem z polaczeniem kod odpowiedzi: " + responseCode);
			}
		} catch (MalformedURLException e) {
			handleException(e);
		}
		if (connection == null) {
			throw new IOException("Obiekt connection typu HttpURLConnection jest nullem");
		}
		return connection.getInputStream();
	}

	private void handleException(Exception ex) {
		if (ex instanceof IOException) {
			ex.printStackTrace();
		}
		if (ex instanceof ParserConfigurationException) {
			System.out.println(ex.getMessage());
		}
	}
}
