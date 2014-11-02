package com.asnl.data;

import com.asnl.beans.Car;
import com.asnl.beans.lowlevel.CarItem;

public class CarTransformer {

	public Car transform(CarItem carItem) {
		Car car = new Car();
		car.setId(carItem.getEi());
		car.setMark(carItem.getMk());
		car.setModel(carItem.getMd());
		car.setYear(carItem.getFr());
		car.setMileage(carItem.getMa());
		String pp = carItem.getPp();
		try {
			String[] split = pp.split(",");
			car.setPrice(split[0].replace(".", ","));
		} catch(Exception e) {
			car.setPrice(pp);
		}
		car.setPriceCurrency(carItem.getCs());
		return car;
	}

}
