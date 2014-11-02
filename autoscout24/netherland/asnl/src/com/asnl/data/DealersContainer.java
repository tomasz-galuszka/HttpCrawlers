package com.asnl.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.asnl.beans.Car;
import com.asnl.beans.Dealer;

public class DealersContainer implements Serializable {

	private static final long serialVersionUID = -4235058141916080579L;
	private Map<Dealer, Set<Car>> offersmap;
	private int page;
	private int totalCount;

	public DealersContainer() {
		offersmap = new HashMap<Dealer, Set<Car>>();
	}

	public void put(Car c) {
		if (c.getDealer() == null) {
			return;
		}
		
		if (!offersmap.containsKey(c.getDealer())) {
			Set<Car> list = new HashSet<Car>();
			list.add(c);
			offersmap.put(c.getDealer(), list);
		} else {
			offersmap.get(c.getDealer()).add(c);
		}
	}

	public Map<Dealer, Set<Car>> getOffersmap() {
		return offersmap;
	}

	public void setOffersmap(Map<Dealer, Set<Car>> offersmap) {
		this.offersmap = offersmap;
	}

	public void printSize() {
		Set<Dealer> keySet = offersmap.keySet();
		int size = 0;
		for (Dealer dealer : keySet) {
			size += offersmap.get(dealer).size();
		}
		System.out.println("Strona: " + page);
		System.out.println("Ilosc stron: " + totalCount);
		float percent = page * 100 / (float)totalCount;
		System.out.println("Pobrano: " + String.format("%.2f", percent) + "%");
		System.out.println("Rozmiar kolekcji: " + size);
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}