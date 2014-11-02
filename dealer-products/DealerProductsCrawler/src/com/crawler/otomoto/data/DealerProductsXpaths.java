package com.crawler.otomoto.data;


public class DealerProductsXpaths {

	public static String pageProductCount = "count(//*[@id='om-list-items']/article[contains(@class, 'om-list')])";
	public static String dealerProductCount = "//*[@id='omListNav']/p[contains(@class, 'om-pager')]/span/a/span/text()";
	
	public static String product = "//*[@id='om-list-items']/*[%d]";
	public static String productLink = ".//h3/a/@href";
	
	public static String productInfo = "//*[@id='omOffer']";
	public static String productPrice = ".//div[@class='om-offer-price']/p[@class='om-price']/span[@class='om-price-primary']/strong[@class='om-price-amount']/text()";
	public static String productPriceCurrency = ".//div[@class='om-offer-price']/p[@class='om-price']/span[@class='om-price-primary']/span[@class='om-price-currency']/text()";
	public static String productModel = ".//div[@class='om-offer-summary']/dl/dd[1]/a[1]/span/text()";
	public static String productType = ".//div[@class='om-offer-summary']/dl/dd[1]/a[2]/span/text()";
	public static String productYear = ".//div[@class='om-offer-summary']/dl/dd[2]/a/span/text()";
	public static String productPrzebieg = ".//div[@class='om-offer-summary']/dl/dd/span[@class='om-mileage-btn-wrapper']/text()";
	
}
