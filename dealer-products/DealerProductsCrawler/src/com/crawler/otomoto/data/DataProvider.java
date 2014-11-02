package com.crawler.otomoto.data;

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

import com.crawler.otomoto.OtoMotoXpathException;
import com.crawler.otomoto.config.ConfigException;
import com.crawler.otomoto.connection.OtoMotoConnectionException;
import com.crawler.otomoto.connection.OtoMotoConnector;
import com.crawler.otomoto.data.beans.DealerItem;
import com.crawler.otomoto.data.beans.ProductItem;

public class DataProvider {

	private OtoMotoConnector connector;

	public List<ProductItem> getOsoboweProducts(DealerItem dealer, int pageNumber) throws OtoMotoConnectionException, OtoMotoXpathException, ConfigException,
			OtoMotoDataException {
		List<ProductItem> products = new ArrayList<ProductItem>();
		String url = dealer.getWww() + "osobowe?p=" + pageNumber;
		Document doc = getConnector().getDocument(url);

		String tmp = (String) evaluateXpath(doc, DealerProductsXpaths.pageProductCount, XPathConstants.STRING);
		Integer count = Integer.valueOf(tmp);

		for (int i = 1; i <= count; i++) {

			Node item = (Node) evaluateXpath(doc, String.format(DealerProductsXpaths.product, i), XPathConstants.NODE);
			String productLink = (String) evaluateXpath(item, DealerProductsXpaths.productLink, XPathConstants.STRING);

			Document productDoc = getConnector().getDocument(dealer.getWww() + "/" + productLink);
			Node pInfonode = (Node) evaluateXpath(productDoc, DealerProductsXpaths.productInfo, XPathConstants.NODE);

			String priceValue = (String) evaluateXpath(pInfonode, DealerProductsXpaths.productPrice, XPathConstants.STRING);
			String priceCurrency = (String) evaluateXpath(pInfonode, DealerProductsXpaths.productPriceCurrency, XPathConstants.STRING);
			String model = StringEscapeUtils.unescapeHtml4((String) evaluateXpath(pInfonode, DealerProductsXpaths.productModel, XPathConstants.STRING));
			String type = (String) evaluateXpath(pInfonode, DealerProductsXpaths.productType, XPathConstants.STRING);
			String year = (String) evaluateXpath(pInfonode, DealerProductsXpaths.productYear, XPathConstants.STRING);
			String przebieg = (String) evaluateXpath(pInfonode, DealerProductsXpaths.productPrzebieg, XPathConstants.STRING);
			if (!przebieg.contains("km")) {
				przebieg = " Nie podano";
			}

			ProductItem product = new ProductItem();
			product.setModelType(model + "/" + type);
			product.setPrice(Double.valueOf(priceValue.replaceAll("\\s+", "")));
			product.setPriceCurrency(priceCurrency);
			product.setYear(year);
			product.setPrzebieg(przebieg);

			products.add(product);
			System.out.println("-- Dodaje oferte: " + product.getModelType());
		}
		return products;
	}

	public int getOsoboweDealerPagesCount(String www) throws OtoMotoXpathException, OtoMotoConnectionException, ConfigException, OtoMotoDataException {
		Document doc = getConnector().getDocument(www + "osobowe");
		NodeList nodes = (NodeList) evaluateXpath(doc, DealerProductsXpaths.dealerProductCount, XPathConstants.NODESET);

		int pages = 0;
		for (int i = 0; i < nodes.getLength(); i++) {
			Node n = nodes.item(i);
			String textContent = n.getTextContent();
			Integer tmp = 0;
			try {
				tmp = Integer.valueOf(textContent);
			} catch (Exception e) {
				continue;
			}
			pages = Math.max(pages, tmp);
		}
		return pages;
	}

	private Object evaluateXpath(Document doc, String xpath, QName xpathConstant) throws OtoMotoDataException {
		try {
			return XPathFactory.newInstance().newXPath().evaluate(xpath, doc, xpathConstant);
		} catch (XPathExpressionException e) {
			throw new OtoMotoDataException("Blad podczas konstrukcji zapytania o dane. Uruchom program w konsoli i sprawdź logi.", e);
		}
	}

	private Object evaluateXpath(Node n, String xpath, QName xpathConstant) throws OtoMotoDataException {
		try {
			return XPathFactory.newInstance().newXPath().evaluate(xpath, n, xpathConstant);
		} catch (XPathExpressionException e) {
			throw new OtoMotoDataException("Blad podczas konstrukcji zapytania o dane. Uruchom program w konsoli i sprawdź logi.", e);
		}
	}

	private OtoMotoConnector getConnector() {
		if (connector == null) {
			connector = new OtoMotoConnector();
		}
		return connector;
	}
}
