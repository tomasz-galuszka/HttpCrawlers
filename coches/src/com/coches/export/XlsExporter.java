package com.coches.export;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.coches.Logger;
import com.coches.crawler.Cars;
import com.coches.data.beans.Car;
import com.coches.data.beans.Dealer;
import com.coches.export.sheet.CarsSheet;
import com.coches.export.sheet.DealersSheet;

public class XlsExporter {

	public void createXls(File f, Cars container) throws CrawlerExportException {
		final int max_sheet_size = 60000;
		int fileNumber = 1;
		int currentSize = 0;
		String filePath = f.getAbsolutePath();

		Map<Dealer, Set<Car>> offers = container.getCarCollection();
		Map<Dealer, Set<Car>> subOffers = new HashMap<Dealer, Set<Car>>();
		Set<Dealer> dealers = offers.keySet();
		for (Dealer dealer : dealers) {
			if (dealer == null) {
				continue;
			}

			int dealerOffers = offers.get(dealer).size();
			if (currentSize + dealerOffers > max_sheet_size) {
				filePath = filePath.replace(".xls", "_" + fileNumber + ".xls");

				Logger.log("-- Eksport to xls: " + filePath);

				export(filePath, subOffers);
				fileNumber++;
				currentSize = 0;
				subOffers.clear();

				Logger.log("OK !");

			} else {
				subOffers.put(dealer, offers.get(dealer));
				currentSize += dealerOffers;
			}
		}

		if (!subOffers.isEmpty()) {
			filePath = filePath.replace(".xls", "_" + fileNumber + "_rest.xls");

			Logger.log("Eksport to xls: " + filePath);

			export(filePath, subOffers);
			fileNumber++;
			currentSize = 0;
			subOffers.clear();

			Logger.log("OK !");
		}
	}

	private void export(String filePath, Map<Dealer, Set<Car>> subOffers) throws CrawlerExportException {
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(new File(filePath));

			new DealersSheet().export(workbook, subOffers);
			new CarsSheet().export(workbook, subOffers);

			workbook.write();
			workbook.close();

		} catch (IOException e) {
			e.printStackTrace();
			throw new CrawlerExportException("Nie można utworzyć pliku xls", e);
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}

}
