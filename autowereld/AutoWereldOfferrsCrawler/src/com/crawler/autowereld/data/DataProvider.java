package com.crawler.autowereld.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.crawler.autowereld.connection.ConnectionException;
import com.crawler.autowereld.connection.Connector;
import com.crawler.autowereld.data.beans.CarItem;
import com.crawler.autowereld.data.beans.DealerItem;
import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;

public class DataProvider {

	private Connector connector;

	public int getDealerPages(String dealerUrl) throws DataException {
		Document doc;
		try {
			doc = getConnector().getDocument(dealerUrl);
		} catch (ConnectionException e1) {
			throw new DataException(" --- Dealer nie posiada danych", e1);
		}
		try {
			checkPageExist(doc);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return 0;
		}

		Node mainNode = (Node) evaluateXpath(doc, CarXpaths.carsContainerXpath, XPathConstants.NODE);
		String tmp = "";
		Integer pages = 0;
		try {
			tmp = (String) evaluateXpath(mainNode, CarXpaths.carPageTotalCountXpath, XPathConstants.STRING);
			pages = Integer.valueOf(tmp);
		} catch (Exception e) {
			try {
				tmp = (String) evaluateXpath(mainNode, CarXpaths.carPageTotalCountLastXpath, XPathConstants.STRING);
				pages = Integer.valueOf(tmp);
			} catch (Exception e2) {
				pages = 1;
			}
		}
		return pages;
	}

	public List<String> getLinksFromPage(String dealerUrl, int page) throws DataException {
		List<String> result = new ArrayList<String>();
		try {
			String www = dealerUrl + "?p=" + page;
			System.out.println(www);
			Document doc = getConnector().getDocument(www);
			checkPageExist(doc);
			Node mainNode = (Node) evaluateXpath(doc, CarXpaths.carsContainerXpath, XPathConstants.NODE);
			DTMNodeList carsTable = (DTMNodeList) evaluateXpath(mainNode, CarXpaths.carPageTablesXpath, XPathConstants.NODESET);
			for (int i = 0; i < carsTable.getLength(); i++) {
				Node table = carsTable.item(i);
				String tmp = (String) evaluateXpath(table, CarXpaths.carPageTableItemSCountXpath, XPathConstants.STRING);
				Integer itemsCount = Integer.valueOf(tmp);

				for (int j = 1; j <= itemsCount; j++) {
					String carUrl = (String) evaluateXpath(table, String.format(CarXpaths.carPageTableItemXpath, j), XPathConstants.STRING);
					result.add(carUrl);
				}
			}
		} catch (NumberFormatException e) {
			throw new DataException("--- Bra ofert na stronie",e);
		} catch (ConnectionException e) {
			throw new DataException(e);
		} catch (Exception e) {
			throw new DataException(e);
		}
		return result;
	}

	public CarItem getCarDetails(DealerItem d, String carUrl) throws DataException {
		CarItem result = new CarItem();
		try {
			Document doc = getConnector().getDocument(Connector.baseUrl + carUrl);
			checkCarPageExist(doc);
			String mark = (String) evaluateXpath(doc, CarXpaths.carDetailsMarkXpath, XPathConstants.STRING);
			String model = (String) evaluateXpath(doc, CarXpaths.carDetailsModelXpath, XPathConstants.STRING);
			String year = (String) evaluateXpath(doc, CarXpaths.carDetailsYearXpath, XPathConstants.STRING);
			String mileage = (String) evaluateXpath(doc, CarXpaths.carDetailsMileageXpath, XPathConstants.STRING);
			String price = (String) evaluateXpath(doc, CarXpaths.carDetailsPriceXpath, XPathConstants.STRING);

			result.setDealer(d);
			result.setMark(mark);
			result.setModel(model);
			result.setYear(year);
			result.setPrzebieg(mileage);
			result.setPrice(price);
			result.setPriceCurrency("€");
		} catch (ConnectionException e) {
			throw new DataException(e);
		}

		return result;

	}

	private void checkCarPageExist(Document doc) throws ConnectionException, DataException {
		try {
			evaluateXpath(doc, CarXpaths.carDetailsMarkXpath, XPathConstants.STRING);
			evaluateXpath(doc, CarXpaths.carDetailsModelXpath, XPathConstants.STRING);
			evaluateXpath(doc, CarXpaths.carDetailsYearXpath, XPathConstants.STRING);
			evaluateXpath(doc, CarXpaths.carDetailsMileageXpath, XPathConstants.STRING);
			evaluateXpath(doc, CarXpaths.carDetailsPriceXpath, XPathConstants.STRING);
			evaluateXpath(doc, CarXpaths.carDetailsPriceCurrencyXpath, XPathConstants.STRING);
		} catch (Exception e) {
			throw new DataException(" --- Strona samochodu nie zawiera danych", e);
		}
	}

	private void checkPageExist(Document doc) throws ConnectionException, DataException {
		try {
			Node mainNode = (Node) evaluateXpath(doc, CarXpaths.carsContainerXpath, XPathConstants.NODE);

			String pages = "";
			try {
				pages = (String) evaluateXpath(mainNode, CarXpaths.carPageTotalCountXpath, XPathConstants.STRING);
				Integer.valueOf(pages);
			} catch (Exception e) {
				try {
					pages = (String) evaluateXpath(mainNode, CarXpaths.carPageTotalCountLastXpath, XPathConstants.STRING);
					Integer.valueOf(pages);
				} catch (Exception e2) {
					pages = "1";
				}
			}

			DTMNodeList carsTable = (DTMNodeList) evaluateXpath(mainNode, CarXpaths.carPageTablesXpath, XPathConstants.NODESET);
			for (int i = 0; i < carsTable.getLength(); i++) {
				Node table = carsTable.item(i);
				String tmp = (String) evaluateXpath(table, CarXpaths.carPageTableItemSCountXpath, XPathConstants.STRING);
				Integer itemsCount = Integer.valueOf(tmp);
				for (int j = 0; j < itemsCount; j++) {
					evaluateXpath(table, String.format(CarXpaths.carPageTableItemXpath, i), XPathConstants.STRING);
				}

			}

		} catch (Exception e) {
			throw new DataException(" --- Dealer nie posiada danych", e);
		}
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

	private Connector getConnector() {
		if (connector == null) {
			connector = new Connector();
		}
		return connector;
	}
}
