package com.coches.data.xpaths;

public class DealerXpath {

	public final static String DEALER_CHECK = "//*[contains(@id, 'datosvendedorProfesional')]";
	
	public final static String WWW = "//*[contains(@id, 'datosvendedorProfesional')]/a/@href";
	public final static String PHONE = "//*[contains(@id, 'datosvendedorProfesional')]/div[contains(@class, 'floatleft')]/div[contains(@class, 'tfno')]/span/text()";
	
	/**
	 * li -number int
	 */
	public final static String ADDRESS_ITEMS = "//*[contains(@id, 'datosvendedorProfesional')]/ul/li[%d]/text()";
	
}
