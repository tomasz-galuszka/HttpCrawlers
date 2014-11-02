package com.asnl.xpaths;

public class CarDetailsPageXpaths {

	public static String dealerInfoContainerXpath = "//div[contains(@id, 'dealerCustomerInfo_contactPanel')]";
	public static String dealerNameXpath1 = ".//div[contains(@id, 'dealerCustomerInfo_contactAddressPanel')]/h2/p";
	public static String dealerNameXpath2 = ".//div[contains(@id, 'dealerCustomerInfo_contactAddressPanel')]/p";
	public static String dealerPhoneXpath = ".//div[contains(@id, 'dealerCustomerInfo_contactPhonePanel')]/div/p/text()";
	public static String dealerAddressXpath = ".//div[contains(@class, 'maps-location')]/table/tbody/tr/td[contains(@class, 'maps-data')]/div[contains(@id, 'mapsIntegration_contactAddressPanel')]/p";
	public static String dealerWwwXpath = ".//div[contains(@id, 'dealerCustomerInfo_sellerDetailsLinkPanel')]/a/@href";
	
	public static String dealerPageTestXpath = "//div[contains(@id, 'detailPagePanel_Panel1')]";
	
}


//detailPagePanel