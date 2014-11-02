package com.asfr.data;

import java.util.ArrayList;
import java.util.List;

import com.asfr.Logger;
import com.asfr.beans.Car;
import com.asfr.beans.lowlevel.CarItem;
import com.asfr.connection.ConnectionException;

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
			Logger.log(e.getMessage());
		} catch (Exception e){
			Logger.log(e.getMessage());
		}
		return cars;
	}
	
	public int getCarsTotalCount() {
		String totalCount = getDataProvider().getTotalCount();
		Integer tc = Integer.valueOf(totalCount);
		int pages = tc / 80;
		pages += tc % 80 > 0 ? 1 : 0;
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
			Logger.log(e.getMessage());
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
