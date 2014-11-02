package com.crawler.autowereld.data.beans;

import java.io.Serializable;

public class DealerItem implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String www;
	private String address;
	private String phone;
	private String auctions;
	private String province;

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
		this.www = www;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
		return "DealerItem [name=" + name + ", www=" + www + ", address=" + address + ", phone=" + phone + ", auctions=" + auctions + "]";
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
}