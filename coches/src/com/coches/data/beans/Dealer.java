package com.coches.data.beans;

import java.io.Serializable;

public class Dealer implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String phone;
	private String www;
	private String street;
	private String code;
	private String city;
	private String province;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWww() {
		return www;
	}

	public void setWww(String www) {
		this.www = www;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Override
	public String toString() {
		return "Dealer [name=" + name + ", phone=" + phone + ", www=" + www + ", street=" + street + ", zipCode=" + code + ", zipCity=" + city + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((www == null) ? 0 : www.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dealer other = (Dealer) obj;
		if (www == null) {
			if (other.www != null)
				return false;
		} else if (!www.equals(other.www))
			return false;
		return true;
	}
}