package com.crawler.autowereld.data.beans;

import com.crawler.autowereld.connection.Connector;

public class DealerItem {

	private String name;
	private String www;
	private String street;
	private String zipCode;
	private String zipCity;
	private String phone;
	private String auctions;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWww() {
		return www;
	}

	public void setWww(String www) {
		this.www = Connector.baseUrl + www;
	}

	public String getZipCity() {
		return zipCity;
	}

	public void setZipCity(String address) {
		this.zipCity = address;
	}

	public String getAuctions() {
		return auctions;
	}

	public void setAuctions(String auctions) {
		this.auctions = auctions;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "DealerItem [name=" + name + ", www=" + www + ", address=" + zipCity + ", phone=" + phone + ", auctions=" + auctions + "]";
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
}