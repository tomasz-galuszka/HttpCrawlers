package com.asfr.data;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.asfr.beans.Car;
import com.asfr.connection.ConnectionException;

public class PageMdtTest {

	private PageMdt pageMdt;

	public void testGetCarItems() {
		// given
		int pageNumber = 2;

		// when
		List<Car> cars = new ArrayList<Car>();
		try {
			cars = getPageMdt().getCars(pageNumber);
		} catch (ConnectionException e) {
			e.printStackTrace();
		}

		// then
		for (Car car : cars) {
//			Logger.log(car);
		}
		Assert.assertEquals(cars.size(), 80);
	}

	public void testPremiumCarsPage() {
		// given

		// when
		List<Car> cars = new ArrayList<Car>();
		try {
			cars = getPageMdt().getPremiumCars();
		} catch (ConnectionException e) {
			e.printStackTrace();
		}

		// then
		for (Car car : cars) {
//			Logger.log(car);
		}
//		Assert.assertEquals(cars.size(), 80);
		
	}
	
	@Test
	public void testGetTotalCount() {
		// given

		// when
		try {
			getPageMdt().getCars(1);
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int carsTotalCount = getPageMdt().getCarsTotalCount();
//		Logger.log(carsTotalCount);
		
		// then
		
	}

	public PageMdt getPageMdt() {
		if (pageMdt == null) {
			pageMdt = new PageMdt();
		}
		return pageMdt;
	}
}
