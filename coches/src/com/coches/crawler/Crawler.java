package com.coches.crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.coches.Logger;
import com.coches.crawler.serialization.CarReader;
import com.coches.crawler.serialization.CarWriter;
import com.coches.crawler.serialization.SerializationException;
import com.coches.data.DataProvider;
import com.coches.data.DataProviderException;
import com.coches.data.Province;
import com.coches.data.beans.Car;

public class Crawler {

	private File tmpFile = new File("downloadedData.dat");
	private DataProvider mdt;
	private CarReader reader;
	private CarWriter writer;
	private Cars cars;

	public void go() throws SerializationException, DataProviderException {
		if (tmpFile.exists()) {
			cars = getReader().read(tmpFile);
		} else {
			cars = new Cars();
		}

		List<Province> provinces = Province.getProvinces();
		int provinceIndex = provinces.indexOf(cars.getLastProvince());
		int lastProvincePage = cars.getLastPage();
		downloadData(provinces, provinceIndex, lastProvincePage);
	}

	private void downloadData(List<Province> provinces, int provinceIndex, int lastProvincePage) throws DataProviderException, SerializationException {
		for (int i = provinceIndex; i < provinces.size(); i++) {
			Province province = provinces.get(provinceIndex);
			String provinceUrl = province.getId();

			int provincePages = 0;
			try {
				provincePages = getMdt().getProvincePages(provinceUrl);
			} catch (DataProviderException e) {
				System.out.println("-- Problem: serwis do not display all pages in province:" + provinceUrl);
				continue;
			}
			Logger.log("Province: " + province.getName());

			for (int j = lastProvincePage + 1; j <= provincePages; j++) {
				Logger.log("Page: " + j);
				fillCarsCollection(province, provinceUrl, j);
				getWriter().write(cars, tmpFile);
			}
		}
	}

	private void fillCarsCollection(Province province, String provinceUrl, int j) throws DataProviderException {
		List<Car> carsFromPage = downloadCarsFromPage(provinceUrl, j);
		for (Car car : carsFromPage) {
			car.getDealer().setProvince(province.getName());
			cars.put(car);
		}
		cars.setLastPage(j);
		cars.setLastProvince(province);
	}

	private List<Car> downloadCarsFromPage(String provinceUrl, int j) throws DataProviderException {
		List<Car> result = new ArrayList<Car>();
		for (Car car : getMdt().getCarsFromPage(provinceUrl, j)) {
			try {
				result.add(getMdt().getDealerAndMArkFromCarDetailPage(car));
			} catch (Exception e) {
				continue;
			}
		}
		return result;
	}

	private CarWriter getWriter() {
		if (writer == null) {
			writer = new CarWriter();
		}
		return writer;
	}

	private CarReader getReader() {
		if (reader == null) {
			reader = new CarReader();
		}
		return reader;
	}

	private DataProvider getMdt() {
		if (mdt == null) {
			mdt = new DataProvider();
		}
		return mdt;
	}

}
