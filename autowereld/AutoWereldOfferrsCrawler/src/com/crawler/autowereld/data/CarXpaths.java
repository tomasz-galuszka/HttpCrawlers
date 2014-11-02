package com.crawler.autowereld.data;

public class CarXpaths {

	public static String carsContainerXpath = "//*[@id='content-inhoud']/div[contains(@class, 'ajaxcontent')]/div[contains(@class, 'aanbiederdetail')]/div[contains(@class, 'resultaatgrid')]";
	public static String carPageTotalCountXpath = ".//div[contains(@class, 'paginatie')]/div/div/ul/li[last()]/a/text()";
	public static String carPageTotalCountLastXpath = ".//div[contains(@class, 'paginatie')]/div/div/ul/li[last()]/strong/text()";
	public static String carPageTablesXpath = ".//table[contains(@class, 'lijst occasion')]";
	public static String carPageTableItemSCountXpath = "count(.//tbody/tr[contains(@class, 'item')])";
	public static String carPageTableItemXpath = ".//tbody/tr[%d]/td[contains(@class, 'omschrijving')]/h3/a/@href";
	
	public static String carContainerXpath = "//*[@id='content-inhoud']/div[contains(@class, 'ajaxcontent')]";
	public static String carDetailsMarkXpath =  carContainerXpath  + "/div[contains(@class, 'kruimelpad')]/a[1]/text()";
	public static String carDetailsModelXpath = carContainerXpath  + "/div[contains(@class, 'kruimelpad')]/a[2]/text()";
	public static String carDetailsYearXpath = carContainerXpath  + "/div[contains(@class, 'occasiondetail')]/div[2]/div[contains(@class, 'hoofdspecs')]/dl/dd[1]";
	public static String carDetailsMileageXpath = carContainerXpath  + "/div[contains(@class, 'occasiondetail')]/div[2]/div[contains(@class, 'hoofdspecs')]/dl/dd[2]";
	public static String carDetailsPriceCurrencyXpath = carContainerXpath  + "/div[contains(@class, 'occasiondetail')]/div[contains(@class, 'header')]/div[contains(@class, 'grid')][1]//div[contains(@class, 'info')]/*";
	public static String carDetailsPriceXpath = carContainerXpath  + "/div[contains(@class, 'occasiondetail')]/div[contains(@class, 'header')]/div[contains(@class, 'grid')][1]/div[contains(@class, 'info')]/span[contains(@class, 'prijs')]/strong/span/text()";
	
}
