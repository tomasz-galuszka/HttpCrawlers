package com.crawler.autovit.data;

public class DealerSetXpaths {

	public static String dealersPerPageXpath = "//*[@id='contentNoBarOM']/div[contains(@class, 'boxOM')]";
	public static String dNameXpath = "//*[@id='contentNoBarOM']/div[%d]/div[@class='boxHeadOM']/h4/a/span/text()";
	public static String dWwwXpath = "//*[@id='contentNoBarOM']/div[%d]/div[@class='boxDescOM']/p[starts-with(text(), 'Vezi oferta completa')]/a/@href";
	public static String dPhoneXpath = "//*[@id='contentNoBarOM']/div[%d]/div[@class='boxDescOM']/p[starts-with(text(), 'Telefon')]/span[1]/span/text()";
	public static String dAddressXpath = "//*[@id='contentNoBarOM']/div[%d]/div[@class='boxDescOM']/p[last()]/text()";
	public static String dealerOffers = "//*[@id='om-app']/div/p[@class='listing-nav'][1]/span[starts-with(text(), 'Numar Anunturi')]/text()";
	
}
