package com.asnl.data;

import java.util.ArrayList;
import java.util.List;

import com.asnl.beans.Car;
import com.asnl.beans.lowlevel.CarItem;
import com.asnl.connection.ConnectionException;

public class PageMdt {

	private DataProvider dataProvider;
	private CarTransformer carTransformer;
	
	public List<Car> getCars(int page) throws ConnectionException {
		List<Car> cars = new ArrayList<Car>();
		try {
			CarItem[] carItems = getDataProvider().getCarItems(page);
			for (int i = 0; i < carItems.length; i++) {
				Car car = getCarTransformer().transform(carItems[i]);
				car.setDealer(getDataProvider().getCarDealer(car.getId()));
				cars.add(car);
			}
		} catch (DataProviderException e) {
			System.out.println(e.getMessage());
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		return cars;
	}
	
	public int getCarsTotalCount() {
		String totalCount = getDataProvider().getTotalCount();
		Integer tc = Integer.valueOf(totalCount);
		int pages = tc / 80;
		pages += tc % 80;
		return pages;
	}
	
	public List<Car> getPremiumCars() throws ConnectionException {
		List<Car> cars = new ArrayList<Car>();
		try {
			CarItem[] carItems = getDataProvider().getPremiumCarItems();
			for (int i = 0; i < carItems.length; i++) {
				Car car = getCarTransformer().transform(carItems[i]);
				car.setDealer(getDataProvider().getPremiumCarDealer(car.getId()));
				cars.add(car);
			}
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		return cars;
	}

	private DataProvider getDataProvider() {
		if (dataProvider ==  null) {
			dataProvider = new DataProvider();
		}
		return dataProvider;
	}
	
	private CarTransformer getCarTransformer() {
		if (carTransformer ==  null) {
			carTransformer = new CarTransformer();
		}
		return carTransformer;
	}
}
