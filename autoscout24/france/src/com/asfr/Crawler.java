package com.asfr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.asfr.beans.Car;
import com.asfr.connection.ConnectionException;
import com.asfr.data.DealersContainer;
import com.asfr.data.PageMdt;

public class Crawler {

	private PageMdt mdt;
	private DealersContainer dataContainer;

	public Crawler() {
		mdt = new PageMdt();
	}

	public void go() {
		if (serializeFileExist()) {
			deserialize();
			for (int i = dataContainer.getPage(); i <= dataContainer.getTotalCount(); i++) {
				downloadNormalCars(i);
			}
		} else {
			dataContainer = new DealersContainer();
			
			downloadPremiumCars();
			
			dataContainer.setPage(1);
			dataContainer.setTotalCount(mdt.getCarsTotalCount());
			
			for (int i = dataContainer.getPage(); i <= dataContainer.getTotalCount(); i++) {
				downloadNormalCars(i);
			}
		}
	}

	public void downloadNormalCars(int page) {
		try {
			List<Car> cars = mdt.getCars(page);
			for (Car car : cars) {
				dataContainer.put(car);
			}
			dataContainer.setPage(page);
			dataContainer.setTotalCount(mdt.getCarsTotalCount());
			serialize();
		} catch (ConnectionException e) {
			Logger.log(e.getMessage());
		}
	}

	public void downloadPremiumCars() {
		try {
			List<Car> premiumCars = mdt.getPremiumCars();
			for (Car car : premiumCars) {
				dataContainer.put(car);
			}
			serialize();
		} catch (ConnectionException e) {
			Logger.log(e.getMessage());
		}
	}

	public DealersContainer getDataContainer() {
		return dataContainer;
	}

	private boolean serializeFileExist() {
		if (new File("fr_downloaded_data.dat").exists()) {
			return true;
		}
		return false;
	}

	private void serialize() {
		dataContainer.printSize();
		Logger.log("-- Serializacja");
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("fr_downloaded_data.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(getDataContainer());
			oos.close();
		} catch (FileNotFoundException e) {
			Logger.log(e.getMessage());
		} catch (IOException e) {
			Logger.log(e.getMessage());
		}
	}

	public void deserialize() {
		if (!serializeFileExist()) {
			return;
		}
		Logger.log("-- Deserializacja");
		try {
			FileInputStream fis = new FileInputStream("fr_downloaded_data.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);
			dataContainer = (DealersContainer) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			Logger.log(e.getMessage());
		} catch (IOException e) {
			Logger.log(e.getMessage());
		} catch (ClassNotFoundException e) {
			Logger.log(e.getMessage());
		}
		dataContainer.printSize();
	}
}
