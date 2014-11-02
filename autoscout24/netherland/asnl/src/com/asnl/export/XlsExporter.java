package com.asnl.export;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.asnl.beans.Car;
import com.asnl.beans.Dealer;
import com.asnl.data.DealersContainer;

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
				filePath.replace(".xls", "_" + fileNumber + ".xls");
				System.out.println("-- Eksport pliku: " + filePath);
				export(filePath, subOffers);
				fileNumber++;
				currentSize = 0;
				subOffers.clear();
				System.out.println("-- OK !");
			} else {
				subOffers.put(dealer, offers.get(dealer));
				currentSize += dealerOffers;
			}
		}
		
		if (!subOffers.isEmpty()) {
			filePath.replace(".xls", "_" + fileNumber + ".xls");
			System.out.println("-- Eksport pliku: " + filePath);
			export(filePath, subOffers);
			fileNumber++;
			currentSize = 0;
			subOffers.clear();
			System.out.println("-- OK !");
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
