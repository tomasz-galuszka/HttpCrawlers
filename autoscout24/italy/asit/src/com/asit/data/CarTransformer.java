package com.asit.data;

import com.asit.beans.Car;
import com.asit.beans.lowlevel.CarItem;

public class CarTransformer {

	public Car transform(CarItem carItem) {
		Car car = new Car();
		car.setId(carItem.getEi());
		car.setMark(carItem.getMk());
		car.setModel(carItem.getMd());
		car.setYear(carItem.getFr());
		car.setMileage(carItem.getMa());
		car.setPrice(carItem.getPp());
		car.setPriceCurrency(carItem.getCs());
		return car;
	}

}
