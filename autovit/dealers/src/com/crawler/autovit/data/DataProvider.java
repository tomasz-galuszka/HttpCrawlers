package com.crawler.autovit.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.crawler.autovit.config.ConfigException;
import com.crawler.autovit.connection.ConnectionException;
import com.crawler.autovit.connection.Connector;
import com.crawler.autovit.data.beans.DealerItem;
import com.crawler.autovit.ui.LogPanel;

public class DataProvider {

	private Connector connector;

	public List<DealerItem> getDealers(int province, int page, LogPanel logger) throws ConnectionException, DataException, ConfigException {
		
		if (province == -1) {
			throw new DataException("Wybrane hrabstwo nie istnieje.");
		}
		
		List<DealerItem> dealers = new ArrayList<DealerItem>();
		Document doc = getConnector().getAllDealersDocument(province, page);
		NodeList nodes = (NodeList) evaluateXpath(doc, DealerSetXpaths.dealersPerPageXpath, XPathConstants.NODESET);

		logger.addInfo("-- Pobrano strone: " + page);
		int add = 0;
		for (int i = 0; i < nodes.getLength() + add; i++) {
			DealerItem item = transformDealer(doc, i);
			if ("".equals(item.getWww()) || item.getWww().equals(null)) {
				add++;
				continue;
			}
			try {
				item.setAuctions(getDealerOffersCount(item.getWww()));
			} catch (ConnectionException e) {
				e.printStackTrace();
				continue;
			}
			dealers.add(item);
			logger.addInfo("- Dodano dealera");
		}
		return dealers;
	}

	private String getDealerOffersCount(String dealerWww) throws DataException, ConnectionException, ConfigException {
		Document doc = getConnector().getDocument(dealerWww);
		String result = "0";
		result = (String) evaluateXpath(doc, DealerSetXpaths.dealerOffers, XPathConstants.STRING);
		try {
			String[] splitted = result.split(":");
			result = splitted[1];
		} catch (Exception e) {
			e.printStackTrace();
			result = "0";
		}
		return result;
	}

	private String fillXpath(String xpath, int i) {
		return String.format(xpath, i);
	}

	private DealerItem transformDealer(Document doc, int i) throws DataException {
		DealerItem dealerItem = new DealerItem();
		dealerItem.setName(StringEscapeUtils.unescapeHtml4((String) evaluateXpath(doc, fillXpath(DealerSetXpaths.dNameXpath, i), XPathConstants.STRING)));
		dealerItem.setWww((String) evaluateXpath(doc, fillXpath(DealerSetXpaths.dWwwXpath, i), XPathConstants.STRING));
		dealerItem.setPhone((String) evaluateXpath(doc, fillXpath(DealerSetXpaths.dPhoneXpath, i), XPathConstants.STRING));
		dealerItem.setAddress(StringEscapeUtils.unescapeHtml4((String) evaluateXpath(doc, fillXpath(DealerSetXpaths.dAddressXpath, i), XPathConstants.STRING)));
		return dealerItem;
	}

	private Object evaluateXpath(Document doc, String xpath, QName xpathConstant) throws DataException {
		try {
			return XPathFactory.newInstance().newXPath().evaluate(xpath, doc, xpathConstant);
		} catch (XPathExpressionException e) {
			throw new DataException("Blad podczas konstrukcji zapytania o dane. Uruchom program w konsoli i sprawdź logi.", e);
		}
	}

	private Connector getConnector() {
		if (connector == null) {
			connector = new Connector();
		}
		return connector;
	}
}
