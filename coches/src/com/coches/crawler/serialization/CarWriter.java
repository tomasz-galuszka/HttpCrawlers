package com.coches.crawler.serialization;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

import com.coches.Logger;
import com.coches.crawler.Cars;
import com.coches.data.beans.Dealer;

public class CarWriter {

	public void write(Cars cars, File f) throws SerializationException {
		try {
			Logger.log("Writting tmp file: " + f.getAbsolutePath());
			Logger.log("Province: " + cars.getLastProvince().getName());
			Logger.log("Page: " + cars.getLastPage());
			
			FileOutputStream fout = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(cars);
			oos.close();
			
			Set<Dealer> set = cars.getCarCollection().keySet();
			int size = 0;
			for (Dealer dealer : set) {
				size += cars.getCarCollection().get(dealer).size();
			}
			Logger.log("Downloaded cars: " + size);
			
			Logger.log("Writting tmp file: SUCCESS !");
		} catch (Exception e) {
			throw new SerializationException("-- Can't writesaved items to file" + f.getAbsolutePath(), e);
		}
	}
}
