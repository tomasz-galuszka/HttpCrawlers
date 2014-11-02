package com.coches.crawler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.coches.data.Province;
import com.coches.data.beans.Car;
import com.coches.data.beans.Dealer;

public class Cars implements Serializable {

	private static final long serialVersionUID = 1L;
	private Map<Dealer, Set<Car>> carCollection;
	private Province lastProvince;
	private int lastPage;

	public Cars() {
		carCollection = new HashMap<Dealer, Set<Car>>();
		lastProvince = Province.A_CORUNA;
		lastPage = 0;
	}

	public Province getLastProvince() {
		return lastProvince;
	}

	public void setLastProvince(Province lastProvince) {
		this.lastProvince = lastProvince;
	}

	public int getLastPage() {
		return lastPage;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	public Map<Dealer, Set<Car>> getCarCollection() {
		return carCollection;
	}

	public void setCarCollection(Map<Dealer, Set<Car>> carCollection) {
		this.carCollection = carCollection;
	}
	
	public void put(Car c) {
		if (c.getDealer() == null) {
			return;
		}
		
		if (!carCollection.containsKey(c.getDealer())) {
			Set<Car> set = new HashSet<Car>();
			set.add(c);
			carCollection.put(c.getDealer(), set);
		} else {
			carCollection.get(c.getDealer()).add(c);
		}
	}

}
