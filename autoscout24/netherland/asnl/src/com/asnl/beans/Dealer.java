package com.asnl.beans;

import java.io.Serializable;

public class Dealer implements Serializable {

	private static final long serialVersionUID = -1414170769096024852L;
	private String name;
	private String phone;
	private String www;
	private String street;
	private String zipCode;
	private String zipCity;

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

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getZipCity() {
		return zipCity;
	}

	public void setZipCity(String zipCity) {
		this.zipCity = zipCity;
	}

	@Override
	public String toString() {
		return "Dealer [name=" + name + ", phone=" + phone + ", www=" + www + ", street=" + street + ", zipCode=" + zipCode + ", zipCity=" + zipCity + "]";
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