package com.asfr.export;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.asfr.Logger;
import com.asfr.beans.Car;
import com.asfr.beans.Dealer;
import com.asfr.data.DealersContainer;

public class XlsExporter {

	public void createXls(String filePath, DealersContainer container) throws CrawlerExportException {
		final int max_sheet_size = 60000;
		int fileNumber = 1;
		int currentSize = 0;
		
		Map<Dealer, Set<Car>> offers = container.getOffersmap();
		Map<Dealer, Set<Car>> subOffers = new HashMap<Dealer, Set<Car>>();
		Set<Dealer> dealers = offers.keySet();
		for (Dealer dealer : dealers) {
			if (dealer == null) {
				continue;
			}

			int dealerOffers = offers.get(dealer).size();
			if (currentSize + dealerOffers > max_sheet_size) {
				filePath = filePath.replace(".xls", "_" + fileNumber + ".xls");
				Logger.log("-- Eksport pliku: " + filePath);
				export(filePath, subOffers);
				fileNumber++;
				currentSize = 0;
				subOffers.clear();
				Logger.log("-- OK !");
			} else {
				subOffers.put(dealer, offers.get(dealer));
				currentSize += dealerOffers;
			}
		}
		
		if (!subOffers.isEmpty()) {
			filePath = filePath.replace(".xls", "_" + fileNumber + "_reszta.xls");
			Logger.log("-- Eksport pliku: " + filePath);
			export(filePath, subOffers);
			fileNumber++;
			currentSize = 0;
			subOffers.clear();
			Logger.log("-- OK !");
		}
	}

	public void export(String filePath, Map<Dealer, Set<Car>> subOffers) throws CrawlerExportException {
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
