package com.asnl.xpaths;

public class SearchResultPageXpaths {

	public static class CAR_LIST_CONTAINER {
		public static String XPATH = "//*[@id='articleListContanier']";
		public static String PREMIUM_LIST_XPATH = XPATH + "/div[@id='premiumListingContainer']/div[@id='topArticleFL']/ul";
		public static String PREMIUM_ITEM_LINK = ".//a/@href";
	}
	
	public static String PAGE_ITEMS = "//*/head/script[contains(., 'articlesFromServer')]/text()";
	
	
}


// //*[@id='articleListContanier']/div[@id='premiumListingContainer']/div[@id='topArticleFL']/ul