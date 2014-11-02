package com.coches.data.xpaths;

public class CarXpath {

	public static final String CAR_LIST_CONTAINER = "//*[@id='container']/div[contains(@id, 'main')]/div[contains(@id, 'search_results')]/div";
	public static final String CAR_COUNT = ".//div[contains(@id, 'search_info')]/div[contains(@id, 'info_results')]/h1/b/text()";
	public static final String CAR_PAGE_COUNT = ".//div[contains(@id, 'grid-rows')]/div[contains(@id, 'rows')]/div[contains(@id, 'gridRows')]/a";

	public static final String PAGE_ID = ".//@href";
	/**
	 * argument string
	 */
	private static final String baseDetailXpath = ".//div[contains(@class, 'datacar')]/div[contains(@class, '%s')]/p/text()";
	private static final String baseDetailPromotedXpath = ".//div[contains(@class, 'datacar')]/div[contains(@class, '%s')]/p/span/text()";

	public static final String MODEL = String.format(baseDetailXpath, "modelo");
	public static final String MODEL_PROMOTED = String.format(baseDetailPromotedXpath, "modelo");

	public static final String MILEAGE = String.format(baseDetailXpath, "km");
	public static final String MILEAGE_PROMOTED = String.format(baseDetailPromotedXpath, "km");

	public static final String YEAR = String.format(baseDetailXpath, "anio");
	public static final String YEAR_PROMOTED = String.format(baseDetailPromotedXpath, "anio");

	public static final String PRICE = String.format(baseDetailXpath, "precio");
	public static final String PRICE_PROMOTED = String.format(baseDetailPromotedXpath, "precio");

	/**
	 * AVAILABLE ON CAR DETAILS PAGE
	 */
	public static final String MARK = "//*[@id='bread']/div[3]/a/span/text()";

}
