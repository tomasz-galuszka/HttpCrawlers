package com.asit.data;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.asit.beans.Dealer;
import com.asit.beans.lowlevel.CarItem;
import com.asit.connection.ConnectionException;
import com.asit.connection.Connector;
import com.asit.xpaths.CarDetailsPageXpaths;
import com.asit.xpaths.SearchResultPageXpaths;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;

public class DataProvider {

	private Connector connector;
	private Gson gson;
	private String totalCount = "2000";

	public Dealer getPremiumCarDealer(String id) throws ConnectionException {
		Document doc = getConnector().getPremiumCarDetailsPage(id);
		if (!itemPageExist(doc)) {
			return null;
		}
		return getDealerFromDocument(doc);
	}

	public Dealer getCarDealer(String id) throws ConnectionException {
		Document doc = getConnector().getNormalCarDetailsPage(id);
		if (!itemPageExist(doc)) {
			return null;
		}
		return getDealerFromDocument(doc);
	}
	
	public CarItem[] getCarItems(int page) throws ConnectionException, DataProviderException {
		Document doc = getConnector().getSearchPage(page);
		String scriptContent = (String) evaluateXpath(doc, SearchResultPageXpaths.PAGE_ITEMS, XPathConstants.STRING);

		CarItem[] cars = {};
		try {
			int startJsVariable = scriptContent.indexOf("var articlesFromServer = [{");
			int endJsVariableIndex = scriptContent.indexOf(";");

			String tmp = scriptContent.substring(startJsVariable, endJsVariableIndex);
			String result = tmp.substring(tmp.indexOf("[{"), tmp.length());

			cars = getGson().fromJson(result, CarItem[].class);
			
			int tcIndex = scriptContent.indexOf("var totalCount = ");
			String tcContent = scriptContent.substring(tcIndex, scriptContent.length());
			int tcEndIndex = tcContent.indexOf(";");
			
			setTotalCount(tcContent.substring(tcContent.indexOf("\"") + 1, tcEndIndex - 1));
		} catch (JsonSyntaxException e) {
			throw new DataProviderException("Parsowanie jsona z treści", e);
		} catch (Exception e) {
			throw new DataProviderException("Parsowanie jsona z treści", e);
		}
		return cars;
	}

	public CarItem[] getPremiumCarItems() throws ConnectionException, DataProviderException {
		String response = getConnector().getPremiumCarsPage(1);
		CarItem[] cars = {};
		try {
			JsonObject obj = new JsonParser().parse(response).getAsJsonObject();
			String jsoninString = obj.get("ta").toString();

			response = jsoninString.substring(1, jsoninString.length() - 1);
			response = response.replace("\\", "");

			cars = getGson().fromJson(response, CarItem[].class);
		} catch (JsonSyntaxException e) {
			throw new DataProviderException("Parsowanie jsona z treści", e);
		} catch (Exception e) {
			throw new DataProviderException("Parsowanie jsona z treści", e);
		}
		return cars;
	};

	private boolean itemPageExist(Document doc) {
		try {
			evaluateXpath(doc, CarDetailsPageXpaths.dealerPageTestXpath, XPathConstants.NODE);
			Node mainNode = (Node) evaluateXpath(doc, CarDetailsPageXpaths.dealerInfoContainerXpath, XPathConstants.NODE);
			evaluateXpath(mainNode, CarDetailsPageXpaths.dealerNameXpath1, XPathConstants.NODESET);
			evaluateXpath(mainNode, CarDetailsPageXpaths.dealerNameXpath2, XPathConstants.NODESET);
			evaluateXpath(mainNode, CarDetailsPageXpaths.dealerAddressXpath, XPathConstants.NODESET);
			evaluateXpath(mainNode, CarDetailsPageXpaths.dealerPhoneXpath, XPathConstants.STRING);
			evaluateXpath(mainNode, CarDetailsPageXpaths.dealerWwwXpath, XPathConstants.STRING);
		} catch (Exception e) {
			return false;
		} 
		return true;
	}

	private Dealer getDealerFromDocument(Document doc) throws ConnectionException {
		Dealer dealer = new Dealer();
		Node mainNode = (Node) evaluateXpath(doc, CarDetailsPageXpaths.dealerInfoContainerXpath, XPathConstants.NODE);

		String dName = "";
		DTMNodeList dealerName1 = (DTMNodeList) evaluateXpath(mainNode, CarDetailsPageXpaths.dealerNameXpath1, XPathConstants.NODESET);
		for (int i = 0; i < dealerName1.getLength(); i++) {
			Node name = dealerName1.item(i);
			if (name.getTextContent().isEmpty()) {
				continue;
			}
			dName += name.getTextContent() + "\n";
		}

		DTMNodeList dealerName2 = (DTMNodeList) evaluateXpath(mainNode, CarDetailsPageXpaths.dealerNameXpath2, XPathConstants.NODESET);
		for (int i = 0; i < dealerName2.getLength(); i++) {
			Node name = dealerName2.item(i);
			if (name.getTextContent().isEmpty()) {
				continue;
			}
			dName += name.getTextContent() + "\n";
		}
		dealer.setName(dName);

		String dAddress = "";
		DTMNodeList dealerAddress = (DTMNodeList) evaluateXpath(mainNode, CarDetailsPageXpaths.dealerAddressXpath, XPathConstants.NODESET);
		for (int i = 0; i < dealerAddress.getLength(); i++) {
			Node address = dealerAddress.item(i);
			if (address.getTextContent().isEmpty()) {
				continue;
			}
			dAddress += address.getTextContent() + "#";
		}
		
		try {
			transformZipData(dealer, dAddress);
		} catch(Exception e ) {
			dealer.setStreet(dAddress);
		}
		
		String dealerPhone = (String) evaluateXpath(mainNode, CarDetailsPageXpaths.dealerPhoneXpath, XPathConstants.STRING);
		dealer.setPhone(dealerPhone);
		String dealerWww = (String) evaluateXpath(mainNode, CarDetailsPageXpaths.dealerWwwXpath, XPathConstants.STRING);
		dealer.setWww(dealerWww);
		return dealer;
	}

	private void transformZipData(Dealer dealer, String dAddress) {
		String[] addressSet = dAddress.split("#");
		dealer.setStreet(addressSet[0]);
		String zipData = addressSet[1];
		dealer.setZipCode(zipData.substring(0, 7));
		dealer.setZipCity(zipData.substring(7, zipData.length()));
	}

	private Object evaluateXpath(Document doc, String xpath, QName xpathConstant) throws ConnectionException {
		try {
			return XPathFactory.newInstance().newXPath().evaluate(xpath, doc, xpathConstant);
		} catch (XPathExpressionException e) {
			handleException(e);
		}
		return null;
	}

	private Object evaluateXpath(Node n, String xpath, QName xpathConstant) throws ConnectionException {
		try {
			return XPathFactory.newInstance().newXPath().evaluate(xpath, n, xpathConstant);
		} catch (XPathExpressionException e) {
			handleException(e);
		}
		return null;
	}

	private void handleException(XPathExpressionException e) throws ConnectionException {
		throw new ConnectionException("Niepoprawne zapytanie xpath.", e);
	}

	private Gson getGson() {
		if (gson == null) {
			gson = new GsonBuilder().create();
		}
		return gson;
	}

	private Connector getConnector() {
		if (connector == null) {
			connector = new Connector();
		}
		return connector;
	}

	public String getTotalCount() {
		return totalCount;
	}

	private void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

}
