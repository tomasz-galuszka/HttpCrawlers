package com.crawler.otomoto.general.connection;

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

public class OtoMotoConnector {

	private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:15.0) Gecko/20120427 Firefox/15.0a1";
	private String baseUrl = "http://otomoto.pl/index.php";
	private String baseQuery = "?sect=search&sub=dealer&search_country=PL&search_province=%d&page=%d";

	public Document getAllDealersDocument(int province, int page) throws OtoMotoConnectionException {
		Document document = null;
		try {
			String requestAddress = baseUrl + String.format(baseQuery, province, page);
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
	
	public Document getDocument(String www) throws OtoMotoConnectionException {
		Document document = null;
		try {
			String response = getResponseAsString(www);
			TagNode cleanedHtml = new HtmlCleaner().clean(response);
			document = new DomSerializer(new CleanerProperties()).createDOM(cleanedHtml);
		} catch (IOException e) {
			handleException(e);
		} catch (ParserConfigurationException e) {
			handleException(e);
		}
		return document;
	}

	private String getResponseAsString(String www) throws IOException, OtoMotoConnectionException {
		BufferedReader bufferedIn = new BufferedReader(new InputStreamReader(connect(www), "UTF-8"));
		String line;
		StringBuilder response = new StringBuilder();
		while ((line = bufferedIn.readLine()) != null) {
			response.append(line);
		}
		bufferedIn.close();
		return response.toString();
	}

	private InputStream connect(String www) throws IOException, OtoMotoConnectionException {
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
			ex.printStackTrace();
		}
	}
}
