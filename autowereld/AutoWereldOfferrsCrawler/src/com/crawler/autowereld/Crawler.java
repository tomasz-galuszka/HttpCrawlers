package com.crawler.autowereld;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.crawler.autowereld.config.ConfigException;
import com.crawler.autowereld.connection.ConnectionException;
import com.crawler.autowereld.data.DataException;
import com.crawler.autowereld.data.DataProvider;
import com.crawler.autowereld.data.beans.CarItem;
import com.crawler.autowereld.data.beans.DealerItem;
import com.crawler.autowereld.export.CrawlerExportException;
import com.crawler.autowereld.export.CrawlerImportException;
import com.crawler.autowereld.export.XlsExporter;
import com.crawler.autowereld.export.XlsImporter;
import com.crawler.autowereld.export.serialization.DealersContainer;
import com.crawler.autowereld.export.serialization.SerializationException;
import com.crawler.autowereld.export.serialization.Serializer;

public class Crawler {

	private Map<Integer, DealerItem> xlsDealers;
	private DealersContainer data;

	public void go(File inputFile) throws Exception {
		xlsDealers = readInputxlsData(inputFile);
		data = readSerializedData(inputFile);
		crawl(inputFile);
	}

	private void crawl(File f) throws Exception {
		Set<Integer> keySet = xlsDealers.keySet();
		int startIndex = data.getCurrentDealerIndexSheet();
		if (startIndex == 0) {
			startIndex = 2;
		} else {
			startIndex += 1;
		}

		
		for (int i = startIndex; i < keySet.size(); i++) {
			DealerItem dealer = xlsDealers.get(i);
			try {

				System.out.println("-- Zaczynam od dealera: " + i);
				System.out.println("-- Pozostało: " + (keySet.size() - i) + " dealerów do pobrania.");

				downloadDealerOffers(dealer);
				data.setCurrentDealerIndexSheet(i);
				saveData(f);

			} catch (Exception e) {
				if (e instanceof ConfigException || e instanceof ConnectionException || e instanceof CrawlerImportException) {
					throw e;
				} else {
					System.out.println("Blad podczas próby pobrania oferty dealera: " + e.getMessage());
					continue;
				}
			}
		}
	}

	private void downloadDealerOffers(DealerItem d) throws DataException {
		System.out.println(" -- Pobieram dane dealera: " + d.getName());

		DataProvider dataProvider = new DataProvider();
		int dealerPages = dataProvider.getDealerPages(d.getWww());
		for (int j = 1; j <= dealerPages; j++) {

			System.out.println("Strona :" + j + " z " + dealerPages);

			List<String> links = dataProvider.getLinksFromPage(d.getWww(), j);
			for (String link : links) {
				CarItem car = dataProvider.getCarDetails(d, link);
				data.add(car);

				System.out.println(" --- " + car.getMark() + " " + car.getModel());
			}
		}
	}

	private void saveData(File f) throws SerializationException, CrawlerImportException {
		System.out.println(" -- Trwa zapis tymczasowy");

		String sheetName = getSheetName(f);
		data.setSheetName(sheetName);
		Serializer serializer = new Serializer(sheetName);
		serializer.save(data);

		System.out.println(" --- Pobrano: " + data.getList().size() + " ofert");
		System.out.println(" --- Ostatni nr dealera: " + data.getCurrentDealerIndexSheet());
		System.out.println(" -- Zapis tymczasowy zakonczony sukcesem !");
	}

	private String getSheetName(File f) throws CrawlerImportException {
		return new XlsImporter(f).getSheetName();
	}

	private Map<Integer, DealerItem> readInputxlsData(File f) throws CrawlerImportException {
		XlsImporter xlsImporter = new XlsImporter(f);
		return xlsImporter.readDealers();
	}

	private DealersContainer readSerializedData(File f) throws SerializationException, CrawlerImportException {
		System.out.println(" -- Trwa odczyt danych");
		Serializer serializer = new Serializer(getSheetName(f));

		if (serializer.existSerializedFile()) {
			DealersContainer data = serializer.read();

			System.out.println(" --- Odczytano: " + data.getList().size() + " ofert");
			System.out.println(" --- Ostatni zapisany numer dealera: " + data.getCurrentDealerIndexSheet());
			System.out.println("-- Odczyt zakonczony sukcesem !");

			return data;
		} else {
			System.out.println("-- Nie ma poprzednio zapisanych danych.");

			return new DealersContainer();
		}
	}

	public void export(File datFile, File xlsFile) throws CrawlerExportException, SerializationException {
		System.out.println(" -- Trwa eksport danych do pliku xls ...");

		Serializer serializer = new Serializer();
		serializer.setFile(datFile);

		if (serializer.existSerializedFile()) {
			data = serializer.read();
		} else {
			System.out.println(" -- Wybrany pilk z danymi nie istnieje !");
			return;
		}

		new XlsExporter().export(xlsFile.getAbsolutePath(), data.getList());

		System.out.println(" -- Eksport zakonczony sukcesem !");
	}
}
