package com.crawler.autowereld.data.beans;

import java.io.Serializable;

public class CarItem implements Serializable {

	private static final long serialVersionUID = 1L;
	private DealerItem dealer;
	private String price;
	private String priceCurrency;
	private String mark;
	private String model;
	private String year;
	private String przebieg;

	public DealerItem getDealer() {
		return dealer;
	}

	public void setDealer(DealerItem dealer) {
		this.dealer = dealer;
	}

	public String getPriceCurrency() {
		return priceCurrency;
	}

	public void setPriceCurrency(String priceCurrency) {
		this.priceCurrency = priceCurrency;
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

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "CarItem [price=" + price + ", priceCurrency=" + priceCurrency + ", mark=" + mark + ", model=" + model + ", year=" + year + ", przebieg="
				+ przebieg + "]";
	}

}
