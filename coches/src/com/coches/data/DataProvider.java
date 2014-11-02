package com.coches.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathConstants;

import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.coches.connection.ConnectionException;
import com.coches.connection.Connector;
import com.coches.data.beans.Car;
import com.coches.data.beans.Dealer;
import com.coches.data.xpaths.CarXpath;
import com.coches.data.xpaths.DealerXpath;
import com.coches.data.xpaths.XpathEvaluator;
import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;

public class DataProvider {

	private Connector connector;
	private XpathEvaluator xpathEvaluator;
	private static final int CARS_PER_PAGE = 30;

	public Car getDealerAndMArkFromCarDetailPage(Car car) throws DataProviderException {
		Car result = car;
		try {
			Document document = getConnector().getDocument(Connector.baseUrl + car.getPageId());

			// checking dealer or single user
			Node mainNode = (Node) getXpathEvaluator().evaluateXpathNode(document, DealerXpath.DEALER_CHECK);
			if (mainNode == null) {
				throw new DataProviderException("-- Car does not belongs to dealer - SKIPPED");
			}

			String carMark = StringEscapeUtils.unescapeHtml4(getXpathEvaluator().evaluateXpathString(document, CarXpath.MARK));
			Dealer dealer = extractDealer(document);

			result.setDealer(dealer);
			result.setMark(carMark);

		} catch (ConnectionException e) {
			throw new DataProviderException("Problem while connecting to page: " + Connector.baseUrl + car.getPageId(), e);
		}
		return result;
	}

	private Dealer extractDealer(Document document) throws DataProviderException {
		String www = getXpathEvaluator().evaluateXpathString(document, DealerXpath.WWW);
		if (www == null || !www.contains("coches.net")) {
			www = "empty";
		}

		String phone = getXpathEvaluator().evaluateXpathString(document, DealerXpath.PHONE);

		String name = StringEscapeUtils.unescapeHtml4(getXpathEvaluator().evaluateXpathString(document, String.format(DealerXpath.ADDRESS_ITEMS, 1)));
		String street = StringEscapeUtils.unescapeHtml4(getXpathEvaluator().evaluateXpathString(document, String.format(DealerXpath.ADDRESS_ITEMS, 2)));
		String cityCode = StringEscapeUtils.unescapeHtml4(getXpathEvaluator().evaluateXpathString(document, String.format(DealerXpath.ADDRESS_ITEMS, 3)));
		cityCode = cityCode.trim();

		Dealer result = new Dealer();
		result.setWww(www);
		result.setPhone(phone.trim());
		result.setName(name.trim());
		result.setStreet(street.trim());

		try {
			String code = cityCode.substring(0, 5);
			String city = cityCode.substring(5, cityCode.length());

			Integer test = Integer.valueOf(code);

			result.setCity(city);
			result.setCode(String.valueOf(test));
		} catch (Exception e) {
			result.setCity(cityCode);
		}
		return result;
	}

	public List<Car> getCarsFromPage(String provinceName, int page) throws DataProviderException {
		List<Car> result = new ArrayList<Car>();
		try {
			String url = String.format(Connector.searchUrl, provinceName, page);
			Document document = getConnector().getDocument(url);
			Node mainNode = (Node) getXpathEvaluator().evaluateXpathNode(document, CarXpath.CAR_LIST_CONTAINER);
			DTMNodeList nodes = (DTMNodeList) getXpathEvaluator().evaluateXpath(mainNode, CarXpath.CAR_PAGE_COUNT, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				Node carNode = nodes.item(i);
				try {
					result.add(extractCar(carNode));
				} catch (DataProviderException e) {
					e.printStackTrace();
					continue;
				}
			}

		} catch (ConnectionException e) {
			throw new DataProviderException("-- Problem while fetching cars from page: " + page, e);
		}
		return result;
	}

	/**
	 * return car without Dealer and mark
	 * 
	 * @param carNode
	 * @return
	 * @throws DataProviderException
	 */
	private Car extractCar(Node carNode) throws DataProviderException {
		String pageId = getXpathEvaluator().evaluateXpathString(carNode, CarXpath.PAGE_ID);

		String model = StringEscapeUtils.unescapeHtml4(getXpathEvaluator().evaluateXpathString(carNode, CarXpath.MODEL));
		if (model == null || model.isEmpty()) {
			model = getXpathEvaluator().evaluateXpathString(carNode, CarXpath.MODEL_PROMOTED);
		}

		String mileage = getXpathEvaluator().evaluateXpathString(carNode, CarXpath.MILEAGE);
		if (mileage == null || mileage.isEmpty()) {
			mileage = getXpathEvaluator().evaluateXpathString(carNode, CarXpath.MILEAGE_PROMOTED);
		}

		String year = getXpathEvaluator().evaluateXpathString(carNode, CarXpath.YEAR);
		if (year == null || year.isEmpty()) {
			year = getXpathEvaluator().evaluateXpathString(carNode, CarXpath.YEAR_PROMOTED);
		}

		String price = getXpathEvaluator().evaluateXpathString(carNode, CarXpath.PRICE);
		if (price == null || price.isEmpty()) {
			price = getXpathEvaluator().evaluateXpathString(carNode, CarXpath.PRICE_PROMOTED);
		}

		Car result = new Car();
		result.setPageId(pageId);
		result.setModel(model);
		result.setMileage(mileage);
		result.setYear(year);
		try {
			String[] priceSplitted = price.split(" ");
			result.setPrice(priceSplitted[0]);
			result.setPriceCurrency(priceSplitted[1]);
		} catch (Exception e) {
			result.setPrice(price);
		}
		return result;
	}

	public int getProvincePages(String provinceUrl) throws DataProviderException {
		int carCount = getCarCountInProvince(provinceUrl);
		int result = carCount / CARS_PER_PAGE;
		if (carCount % CARS_PER_PAGE != 0) {
			++result;
		}
		return result;
	}

	private int getCarCountInProvince(String provinceUrl) throws DataProviderException {
		int result = 0;
		try {

			String url = String.format(Connector.searchUrl, provinceUrl, 1);
			Document document = getConnector().getDocument(url);
			Node mainNode = (Node) getXpathEvaluator().evaluateXpathNode(document, CarXpath.CAR_LIST_CONTAINER);

			String carCount = (String) getXpathEvaluator().evaluateXpath(mainNode, CarXpath.CAR_COUNT, XPathConstants.STRING);
			carCount = carCount.replace(".", "");

			try {
				result = Integer.valueOf(carCount);
			} catch (NumberFormatException e) {
				result = 0;
			}

		} catch (ConnectionException e) {
			throw new DataProviderException("-- Error while counting car in current province", e);
		}
		return result;
	}

	private Connector getConnector() {
		if (connector == null) {
			connector = new Connector();
		}
		return connector;
	}

	private XpathEvaluator getXpathEvaluator() {
		if (xpathEvaluator == null) {
			xpathEvaluator = new XpathEvaluator();
		}
		return xpathEvaluator;
	}
}
