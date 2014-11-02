package com.crawler.autowereld.data;

public class DealerSetXpaths {

	public static String dealersPerPageXpath = "//*[@id='content-inhoud']/div[contains(@class, 'ajaxcontent')]/div[contains(@class, 'resultaatgrid')]/table[contains(@class, 'lijst')]";
	public static String dealersInPageTable = "count(.//tbody/tr[contains(@class, 'item')])";
	
	public static String dealerPagesInProvince = "//*[@id='content-inhoud']/div[contains(@class, 'ajaxcontent')]/div[contains(@class, 'resultaatgrid')]/div[contains(@class, 'paginatie')]/div[contains(@class, 'paginator')]/div/ul/li[last()]/a/text()";
	
	
	public static String dNameXpath = ".//tbody/tr[contains(@class, 'item')][%d]/td[contains(@class, 'contact')]/h3/a/text()";
	public static String dWwwXpath = ".//tbody/tr[contains(@class, 'item')][%d]/td[contains(@class, 'contact')]/h3/a/@href";
	
	// wymaga odwiedzenia strony
	public static String dPhoneXpath = "//*[@id='content-inhoud']/div[contains(@class, 'ajaxcontent')]/div[contains(@class, 'aanbiederdetail')]"
			+ "/div[contains(@class, 'header')]/div/div[contains(@class, 'info')]/div[contains(@class, 'specs')]/a[contains(@class, 'telefoon')]"
			+ "/@data-telefoon";
	
	public static String dAddressXpath = ".//tbody/tr[contains(@class, 'item')][%d]/td[contains(@class, 'contact')]/a/text()";
	public static String dealerOffers = ".//tbody/tr[contains(@class, 'item')][%d]/td[contains(@class, 'occasionaantal')]/a/text()";
	
}
//