package com.crawler.autovit;

import java.io.File;
import java.util.List;

import com.crawler.autovit.connection.ConnectionException;
import com.crawler.autovit.data.DataException;
import com.crawler.autovit.data.DataProvider;
import com.crawler.autovit.data.beans.DealerItem;
import com.crawler.autovit.export.XlsExporter;
import com.crawler.autovit.export.XlsImporter;
import com.crawler.autovit.export.XlsPackage;

public class Crawler {

	private XlsImporter xlsImporter;
	private XlsExporter xlsExporter;
	private String inputProvinceXls;
	private DataProvider mdt;
	private String provinceName;

	public Crawler(String inputProvinceXls) {
		this.inputProvinceXls = inputProvinceXls;
	}

	public void doCrawl() throws CrawlerException, DataException {
		provinceName = getInputXls().getSheetName();

		List<DealerItem> dealers = getInputXls().geatDealers();
		System.out.println(" -- Odczytano liste dealerow z pliku wejsciowego");

		String lastExportedDealerName = "XXX";

		if (getOutputXls().getLastExportedDealerProvince() == null) {
			lastExportedDealerName = null;
		} else if (getOutputXls().getLastExportedDealerProvince().compareTo(provinceName) == 0) {
			lastExportedDealerName = getLastExportedDealerName();
			removeLastExportedDealerFromXls(lastExportedDealerName);
		}

		try {
			if (lastExportedDealerName == null) {
				System.out.println(" -- Pobieram dane od początku");
				crawlFromStart(provinceName, dealers);
			} else {
				System.out.println(" -- Pobieram dane od ostatniego w pełni pobranego dealera");
				crawlFromLast(provinceName, dealers, lastExportedDealerName);
			}
		} catch (ConnectionException e) {
			throw new CrawlerException("Wystąpił błąd podczas pobierania danych", e);
		}

	}

	private void crawlFromLast(String provinceName, List<DealerItem> dealers, String lastDealerName) throws ConnectionException, CrawlerException, DataException {
		boolean start = false;
		for (DealerItem dealer : dealers) {
			if (!start) {
				if (dealer.getName().compareTo(lastDealerName) == 0 || lastDealerName.compareTo("XXX") == 0) {
					System.out.println("-- Zacznynam od dealera: " + dealer.getName());
					start = true;
				}
			} else {
				XlsPackage xlsPackage = new XlsPackage();
				xlsPackage.setProvince(provinceName);
				xlsPackage.setDealer(dealer);
				System.out.println("\n=== Strona dealera: " + dealer.getWww() + "===");
				int pages;
				try {
					pages = getMdt().getOsoboweDealerPagesCount(dealer.getWww());
				} catch (DataException e) {
					System.out.println(e.getMessage());
					System.out.println("-- \nPrzechodze do kolejnego dealera\n");
					continue;
				}
				System.out.println("-- Ilość stron do przeszukania: " + pages + "\n");

				for (int i = 1; i <= pages; i++) {
					System.out.println("- Strona nr: " + i);
					try {
						xlsPackage.setProducts(getMdt().getOsoboweProducts(dealer, i));
					} catch (DataException e) {
						System.out.println(e.getMessage());
						System.out.println("-- \nPrzechodze do kolejnej strony\n");
						continue;
					}
					xlsExporter.appendToFile(xlsPackage);
				}
			}
		}
	}

	private void crawlFromStart(String provinceName, List<DealerItem> dealers) throws ConnectionException, CrawlerException, DataException {
		for (DealerItem dealer : dealers) {

			XlsPackage xlsPackage = new XlsPackage();
			xlsPackage.setProvince(provinceName);
			xlsPackage.setDealer(dealer);
			System.out.println("-- Strona dealera: " + dealer.getWww());

			int pages = 0;
			try {
				pages = getMdt().getOsoboweDealerPagesCount(dealer.getWww());
			} catch (DataException e) {
				System.out.println(e.getMessage());
				System.out.println("-- \nPrzechodze do kolejnego dealera\n");
				continue;
			}
			System.out.println("-- Ilość stron do przeszukania: " + pages);

			for (int i = 1; i <= pages; i++) {
				System.out.println("- Strona nr: " + i);
				try {
					xlsPackage.setProducts(getMdt().getOsoboweProducts(dealer, i));
				} catch (DataException e) {
					System.out.println(e.getMessage());
					System.out.println("-- \nPrzechodze do kolejnej strony\n");
					continue;
				}
				xlsExporter.appendToFile(xlsPackage);
			}
		}
	}

	private void removeLastExportedDealerFromXls(String lastDealerName) throws CrawlerException {
		getOutputXls().removeTrailingRows(lastDealerName);
	}

	private String getLastExportedDealerName() throws CrawlerException {
		return getOutputXls().getLastExportedDealerName();
	}

	private XlsImporter getInputXls() throws CrawlerException {
		if (xlsImporter == null) {
			xlsImporter = new XlsImporter(new File(inputProvinceXls));
		}
		return xlsImporter;
	}

	private DataProvider getMdt() {
		if (mdt == null) {
			mdt = new DataProvider();
		}
		return mdt;
	}

	private XlsExporter getOutputXls() {
		if (xlsExporter == null) {
			xlsExporter = new XlsExporter(provinceName + ".xls");
		}
		return xlsExporter;
	}
}
