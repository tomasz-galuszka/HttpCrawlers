package com.crawler.autowereld.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.crawler.autowereld.config.ConfigException;
import com.crawler.autowereld.connection.ConnectionException;
import com.crawler.autowereld.connection.Connector;
import com.crawler.autowereld.data.beans.DealerItem;
import com.crawler.autowereld.ui.LogPanel;

public class DataProvider {

	private Connector connector;

	public List<DealerItem> getDealers(String province, int page, LogPanel logger) throws ConnectionException, ConfigException, DataException {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		Document doc = getConnector().getAllDealersDocument(province, page);
		NodeList nodes = (NodeList) evaluateXpath(doc, DealerSetXpaths.dealersPerPageXpath, XPathConstants.NODESET);
		logger.addInfo("-- Pobrano strone: " + page);
		List<DealerItem> dealers = fetchDealerList(logger, nodes);
		return dealers;
	}

	private List<DealerItem> fetchDealerList(LogPanel logger, NodeList nodes) throws ConnectionException, ConfigException, NumberFormatException, DataException {
		List<DealerItem> dealers = new ArrayList<DealerItem>();
		int add = 0;
		for (int i = 0; i < nodes.getLength() + add; i++) {
			Node tableNode = nodes.item(i);
			int dealersInTable = Integer.valueOf((String) evaluateXpath(tableNode, DealerSetXpaths.dealersInPageTable, XPathConstants.STRING));
			for (int j = 1; j <= dealersInTable; j++) {
				DealerItem item = transformDealer(tableNode, j);
				if ("".equals(item.getWww()) || item.getWww().equals(null)) {
					add++;
					continue;
				}
				try {
					item.setPhone(getDealerPhoneNumber(item.getWww()));
				} catch (Exception e) {
					System.out.println(" ### " + e.getMessage());
					continue;
				}
				dealers.add(item);
				logger.addInfo("- Dodano dealera");
			}
		}
		return dealers;
	}

	private String getDealerPhoneNumber(String dealerWww) throws ConnectionException, ConfigException, DataException {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		Document doc = getConnector().getDocument(dealerWww);
		String phoneString = (String) evaluateXpath(doc, DealerSetXpaths.dPhoneXpath, XPathConstants.STRING);
		return phoneString.replace("Tel.", "");
	}

	private DealerItem transformDealer(Node node, int i) throws ConnectionException, DataException {
		DealerItem dealerItem = new DealerItem();
		dealerItem.setName(StringEscapeUtils.unescapeHtml4((String) evaluateXpath(node, fillXpath(DealerSetXpaths.dNameXpath, i), XPathConstants.STRING)));
		dealerItem.setWww((String) evaluateXpath(node, fillXpath(DealerSetXpaths.dWwwXpath, i), XPathConstants.STRING));
		dealerItem.setAuctions((String) evaluateXpath(node, fillXpath(DealerSetXpaths.dealerOffers, i), XPathConstants.STRING));

		NodeList nodes = (NodeList) evaluateXpath(node, fillXpath(DealerSetXpaths.dAddressXpath, i), XPathConstants.NODESET);

		if (nodes.getLength() == 1) {
			dealerItem.setZipCity(StringEscapeUtils.unescapeHtml4(nodes.item(0).getNodeValue()));
			return dealerItem;
		}

		try {
			dealerItem.setStreet(StringEscapeUtils.unescapeHtml4(nodes.item(0).getNodeValue()));
		} catch (Exception e) {
			dealerItem.setStreet("");
		}

		// 4 cyfry spacja dwie litery 4444AB
		String zipData = nodes.item(1).getNodeValue();
		try {
			zipData = zipData.replace(" ", "");
			String zipCode = zipData.substring(0, 4) + " " + zipData.substring(4, 6);
			String zipCity = zipData.substring(6, zipData.length());
			dealerItem.setZipCity(StringEscapeUtils.unescapeHtml4(zipCity));
			dealerItem.setZipCode(StringEscapeUtils.unescapeHtml4(zipCode));
		} catch (Exception e) {
			dealerItem.setZipCity(zipData);
			dealerItem.setZipCode("");
		}

		return dealerItem;
	}

	private Object evaluateXpath(Document doc, String xpath, QName xpathConstant) throws DataException {
		try {
			return XPathFactory.newInstance().newXPath().evaluate(xpath, doc, xpathConstant);
		} catch (XPathExpressionException e) {
			throw new DataException("Blad podczas konstrukcji zapytania o dane. Uruchom program w konsoli i sprawdź logi.", e);
		}
	}

	private Object evaluateXpath(Node n, String xpath, QName xpathConstant) throws DataException {
		try {
			return XPathFactory.newInstance().newXPath().evaluate(xpath, n, xpathConstant);
		} catch (XPathExpressionException e) {
			throw new DataException("Blad podczas konstrukcji zapytania o dane. Uruchom program w konsoli i sprawdź logi.", e);
		}
	}

	private String fillXpath(String xpath, int i) {
		return String.format(xpath, i);
	}

	private Connector getConnector() {
		if (connector == null) {
			connector = new Connector();
		}
		return connector;
	}
}
