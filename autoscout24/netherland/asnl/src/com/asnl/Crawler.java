package com.asnl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.asnl.beans.Car;
import com.asnl.connection.ConnectionException;
import com.asnl.data.DealersContainer;
import com.asnl.data.PageMdt;

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
			System.out.println(e.getMessage());
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
			System.out.println(e.getMessage());
		}
	}

	public DealersContainer getDataContainer() {
		return dataContainer;
	}

	private boolean serializeFileExist() {
		if (new File("downloaded_data.dat").exists()) {
			return true;
		}
		return false;
	}

	private void serialize() {
		dataContainer.printSize();
		System.out.println("-- Serializacja");
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("downloaded_data.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(getDataContainer());
			oos.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void deserialize() {
		if (!serializeFileExist()) {
			return;
		}
		System.out.println("-- Deserializacja");
		try {
			FileInputStream fis = new FileInputStream("downloaded_data.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);
			dataContainer = (DealersContainer) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		dataContainer.printSize();
	}
}
