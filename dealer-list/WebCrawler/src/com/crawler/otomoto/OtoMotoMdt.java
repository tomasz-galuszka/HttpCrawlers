package com.crawler.otomoto;

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

import com.crawler.otomoto.dealer.list.DealerItem;
import com.crawler.otomoto.dealer.list.DealerSetXpaths;
import com.crawler.otomoto.dealer.list.gui.LogPanel;
import com.crawler.otomoto.dealer.single.DealerProductsXpaths;
import com.crawler.otomoto.dealer.single.ProductItem;
import com.crawler.otomoto.general.connection.OtoMotoConnectionException;
import com.crawler.otomoto.general.connection.OtoMotoConnector;

public class OtoMotoMdt {

	private OtoMotoConnector connector;

	private String fillXpath(String xpath, int i) {
		return String.format(xpath, i);
	}

	public List<DealerItem> getDealers(int province, int page, LogPanel logger) throws OtoMotoConnectionException {
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
			} catch(OtoMotoConnectionException e) {
				e.printStackTrace();
				continue;
			}
			dealers.add(item);
			logger.addInfo("- Dodano dealera");
		}
		return dealers;
	}

	public String getDealerOffersCount(String dealerWww) throws OtoMotoConnectionException {
		Document doc = getConnector().getDocument(dealerWww);
		String result = "0";
		result = (String) evaluateXpath(doc, DealerSetXpaths.dealerOffers, XPathConstants.STRING);
		try {
			String[] splitted = result.split(":");
			result = splitted[1];
		} catch(Exception e) {
			result = (String) evaluateXpath(doc, DealerSetXpaths.dealerOffers, XPathConstants.STRING);
			e.printStackTrace();
		}
		return result;
	}

	public List<ProductItem> getOsoboweProducts(DealerItem dealer, int pageNumber) throws OtoMotoConnectionException {
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
				przebieg = "Nie podano";
			}
			
			ProductItem product = new ProductItem();
			product.setModelType(model + "/" + type);
			product.setPrice(Double.valueOf(priceValue.replaceAll("\\s+","")));
			product.setPriceCurrency(priceCurrency);
			product.setYear(year);
			product.setPrzebieg(przebieg);
			
			products.add(product);
			System.out.println(product);
		}
		System.out.println("Dealer: "+ dealer.getName() +"Lista produktow: " + products.size());
		return products;
	}
	
	public int getOsoboweProductsPagesCount(String www) throws OtoMotoConnectionException {
		String url;
		Document doc;
		int p = 1;
		while (true) {
			url  = www + "/osobowe?p=" + p;
			doc = getConnector().getDocument(url);
			String tmp = (String) evaluateXpath(doc, DealerProductsXpaths.pageProductCount, XPathConstants.STRING);
			Integer count = Integer.valueOf(tmp);
			if (count == 0) {
				break;
			}
			p++;
		}
		return p -1;
	}
	
	private DealerItem transformDealer(Document doc, int i) throws OtoMotoConnectionException {
		DealerItem dealerItem = new DealerItem();
		dealerItem.setName(StringEscapeUtils.unescapeHtml4((String) evaluateXpath(doc, fillXpath(DealerSetXpaths.dNameXpath, i), XPathConstants.STRING)));
		dealerItem.setWww((String) evaluateXpath(doc, fillXpath(DealerSetXpaths.dWwwXpath, i), XPathConstants.STRING));
		dealerItem.setPhone((String) evaluateXpath(doc, fillXpath(DealerSetXpaths.dPhoneXpath, i), XPathConstants.STRING));
		dealerItem.setAddress(StringEscapeUtils.unescapeHtml4((String) evaluateXpath(doc, fillXpath(DealerSetXpaths.dAddressXpath, i), XPathConstants.STRING)));
		return dealerItem;
	}

	private Object evaluateXpath(Document doc, String xpath, QName xpathConstant) throws OtoMotoConnectionException {
		try {
			return XPathFactory.newInstance().newXPath().evaluate(xpath, doc, xpathConstant);
		} catch (XPathExpressionException e) {
			handleException(e);
		}
		return null;
	}
	
	private Object evaluateXpath(Node n, String xpath, QName xpathConstant) throws OtoMotoConnectionException {
		try {
			return XPathFactory.newInstance().newXPath().evaluate(xpath, n, xpathConstant);
		} catch (XPathExpressionException e) {
			handleException(e);
		}
		return null;
	}

	private void handleException(XPathExpressionException e) throws OtoMotoConnectionException {
		e.printStackTrace();
		throw new OtoMotoConnectionException("Niepoprawne zapytanie xpath.", e);
	}

	private OtoMotoConnector getConnector() {
		if (connector == null) {
			connector = new OtoMotoConnector();
		}
		return connector;
	}
}
