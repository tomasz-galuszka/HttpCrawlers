package com.crawler.otomoto.dealer.list;

public class DealerSetXpaths {

	public static String dealersPerPageXpath = "//*[@id='contentNoBarOM']/div[contains(@class, 'boxOM')]";
	public static String dNameXpath = "//*[@id='contentNoBarOM']/div[%d]/div[@class='boxHeadOM']/h4/a/span/text()";
	public static String dWwwXpath = "//*[@id='contentNoBarOM']/div[%d]/div[@class='boxDescOM']/p[starts-with(text(), 'Adres strony')]/a/@href";
	public static String dPhoneXpath = "//*[@id='contentNoBarOM']/div[%d]/div[@class='boxDescOM']/p[starts-with(text(), 'Telefon')]/span[1]/span/text()";
	public static String dAddressXpath = "//*[@id='contentNoBarOM']/div[%d]/div[@class='boxDescOM']/p[last()]/text()";
	public static String dealerOffers = "//*[@id='om-app']/div/p[@class='listing-nav']/span[starts-with(text(), 'Liczba ogłoszeń:')]/text()";
	
}
