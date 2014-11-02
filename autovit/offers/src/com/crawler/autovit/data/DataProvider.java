package com.crawler.autovit.data;

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

import com.crawler.autovit.connection.ConnectionException;
import com.crawler.autovit.connection.Connector;
import com.crawler.autovit.data.beans.DealerItem;
import com.crawler.autovit.data.beans.ProductItem;

public class DataProvider {

	private Connector connector;

	public List<ProductItem> getOsoboweProducts(DealerItem dealer, int pageNumber) throws ConnectionException, DataException {
		Document doc = getConnector().getDocument(prepareOsoboweUrl(dealer, pageNumber));

		List<ProductItem> products1 = new ArrayList<ProductItem>();
		List<ProductItem> products2 = new ArrayList<ProductItem>();
		List<ProductItem> products3 = new ArrayList<ProductItem>();

		Integer promotedCount = getPromotedOffersCount(doc);
		boolean pageWithPromoted = false;
		if (promotedCount != 0) {
			pageWithPromoted = true;
			products1 = downloadOffer(dealer, doc, promotedCount, DealerProductsXpaths.productPromoted);
		}

		if (pageWithPromoted) {
			products2 = downloadOffer(dealer, doc, getProductWithPromotedOffersCount(doc), DealerProductsXpaths.productWithPromoted);
		} else {
			products3 = downloadOffer(dealer, doc, getOsoboweOffersCount(doc), DealerProductsXpaths.product);
		}

		List<ProductItem> result = new ArrayList<ProductItem>();
		result.addAll(products1);
		result.addAll(products2);
		result.addAll(products3);
		return result;
	}

	private List<ProductItem> downloadOffer(DealerItem dealer, Document doc, Integer count, String xpathProduct) throws DataException, ConnectionException {
		List<ProductItem> products = new ArrayList<ProductItem>();
		for (int i = 1; i <= count; i++) {

			Node item = (Node) evaluateXpath(doc, String.format(xpathProduct, i), XPathConstants.NODE);
			String productLink = (String) evaluateXpath(item, DealerProductsXpaths.productLink, XPathConstants.STRING);

			Document productDoc = getConnector().getDocument(prepareOfferUrl(dealer, productLink));
			Node pInfonode = (Node) evaluateXpath(productDoc, DealerProductsXpaths.productInfo, XPathConstants.NODE);

			String priceValue = (String) evaluateXpath(pInfonode, DealerProductsXpaths.productPrice, XPathConstants.STRING);
			String priceCurrency = (String) evaluateXpath(pInfonode, DealerProductsXpaths.productPriceCurrency, XPathConstants.STRING);
			String model = StringEscapeUtils.unescapeHtml4((String) evaluateXpath(pInfonode, DealerProductsXpaths.productModel, XPathConstants.STRING));
			String type = (String) evaluateXpath(pInfonode, DealerProductsXpaths.productType, XPathConstants.STRING);
			String year = (String) evaluateXpath(pInfonode, DealerProductsXpaths.productYear, XPathConstants.STRING);
			String przebieg = (String) evaluateXpath(pInfonode, DealerProductsXpaths.productPrzebieg, XPathConstants.STRING);
			if (!przebieg.contains("km")) {
				przebieg = " -- ";
			}

			ProductItem product = new ProductItem();
			product.setModelType(model + " " + type);
			product.setPrice(Double.valueOf(priceValue.replaceAll("\\s+", "")));
			product.setPriceCurrency(priceCurrency);
			product.setYear(year);
			product.setPrzebieg(przebieg);

			products.add(product);
			System.out.println("-- Dodaje oferte: " + product.getModelType());
		}
		return products;
	}

	private Integer getPromotedOffersCount(Document doc) throws DataException {
		Integer count = 0;
		try {
			count = Integer.valueOf((String) evaluateXpath(doc, DealerProductsXpaths.pageProductPromotedCount, XPathConstants.STRING));

		} catch (NumberFormatException e) {
			throw new DataException("Liczba ofert dealera na stronie nie jest liczbą", e);
		}
		return count;
	}

	private Integer getProductWithPromotedOffersCount(Document doc) throws DataException {
		Integer count = 0;
		try {
			count += Integer.valueOf((String) evaluateXpath(doc, DealerProductsXpaths.pageProductWithPromotedCount, XPathConstants.STRING));

		} catch (NumberFormatException e) {
			throw new DataException("Liczba ofert dealera na stronie nie jest liczbą", e);
		}
		return count;
	}

	private Integer getOsoboweOffersCount(Document doc) throws DataException {
		Integer count = 0;
		try {

			count = Integer.valueOf((String) evaluateXpath(doc, DealerProductsXpaths.pageProductCount, XPathConstants.STRING));

		} catch (NumberFormatException e) {
			throw new DataException("Liczba ofert dealera na stronie nie jest liczbą", e);
		}
		return count;
	}

	private String prepareOfferUrl(DealerItem dealer, String productLink) {
		String url = dealer.getWww() + "/" + productLink;
		url = url.replace("///", "/");
		return url;
	}

	private String prepareOsoboweUrl(DealerItem dealer, int pageNumber) {
		return dealer.getWww() + "autoturisme?p=" + pageNumber;
	}

	public int getOsoboweDealerPagesCount(String www) throws ConnectionException, DataException {
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
