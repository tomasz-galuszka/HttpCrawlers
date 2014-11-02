package com.crawler.otomoto.data.beans;

public class ProductItem {

	private DealerItem dealer;
	private Double price;
	private String priceCurrency;
	private String modelType;
	private String year;
	private String przebieg;

	public DealerItem getDealer() {
		return dealer;
	}

	public void setDealer(DealerItem dealer) {
		this.dealer = dealer;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getPriceCurrency() {
		return priceCurrency;
	}

	public void setPriceCurrency(String priceCurrency) {
		this.priceCurrency = priceCurrency;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getPrzebieg() {
		return przebieg;
	}

	public void setPrzebieg(String przebieg) {
		this.przebieg = przebieg;
	}

	@Override
	public String toString() {
		return "ProductItem [price=" + price + ", priceCurrency=" + priceCurrency + ", modelType=" + modelType + ", year=" + year + ", przebieg=" + przebieg
				+ "]";
	}
}
