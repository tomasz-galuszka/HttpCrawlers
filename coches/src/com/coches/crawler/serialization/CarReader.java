package com.coches.crawler.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Set;

import com.coches.Logger;
import com.coches.crawler.Cars;
import com.coches.data.beans.Dealer;

public class CarReader {

	public Cars read(File f) throws SerializationException {
		try {
			Logger.log("Reading input tmp file: " + f.getAbsolutePath());

			FileInputStream fin = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fin);
			Cars result = (Cars) ois.readObject();
			ois.close();

			Logger.log("Reading input tmp file: SUCCESS !");
			Logger.log("Last province: " + result.getLastProvince());
			Logger.log("Last page: " + result.getLastPage());

			Set<Dealer> set = result.getCarCollection().keySet();
			int size = 0;
			for (Dealer dealer : set) {
				size += result.getCarCollection().get(dealer).size();
			}
			Logger.log("Downloaded cars: " + size);

			return result;
		} catch (Exception e) {
			throw new SerializationException("-- Can't read saved items form file" + f.getAbsolutePath(), e);
		}
	}

}
