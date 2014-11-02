package com.crawler.otomoto;

import java.io.File;
import java.util.List;

import com.crawler.otomoto.config.ConfigException;
import com.crawler.otomoto.connection.OtoMotoConnectionException;
import com.crawler.otomoto.data.DataProvider;
import com.crawler.otomoto.data.OtoMotoDataException;
import com.crawler.otomoto.data.beans.DealerItem;
import com.crawler.otomoto.export.XlsExporter;
import com.crawler.otomoto.export.XlsImporter;
import com.crawler.otomoto.export.XlsPackage;

public class OtoMotoCrawler {

	private XlsImporter xlsImporter;
	private XlsExporter xlsExporter;
	private String inputProvinceXls;
	private DataProvider mdt;
	private String provinceName;

	public OtoMotoCrawler(String inputProvinceXls) {
		this.inputProvinceXls = inputProvinceXls;
	}

	public void doCrawl() throws OtoMotoCrawlerException, ConfigException, OtoMotoDataException {
		provinceName = getInputXls().getSheetName();
		List<DealerItem> dealers = getInputXls().geatDealers();

		String lastExportedDealerName = "XXX";
		if (getOutputXls().getLastExportedDealerProvince() == null ) {
			lastExportedDealerName = null;
		} else if (getOutputXls().getLastExportedDealerProvince().compareTo(provinceName) == 0) {
			lastExportedDealerName = getLastExportedDealerName();
			removeLastExportedDealerFromXls(lastExportedDealerName);
		}

		try {
			if (lastExportedDealerName == null) {
				crawlFromStart(provinceName, dealers);
			} else {
				crawlFromLast(provinceName, dealers, lastExportedDealerName);
			}
		} catch (OtoMotoConnectionException e) {
			throw new OtoMotoCrawlerException("Wystąpił błąd podczas pobierania danych", e);
		}

	}

	private void crawlFromLast(String provinceName, List<DealerItem> dealers, String lastDealerName) throws OtoMotoConnectionException,
			OtoMotoCrawlerException, ConfigException, OtoMotoDataException {
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
				} catch (OtoMotoXpathException e) {
					System.out.println(e.getMessage());
					System.out.println("-- \nPrzechodze do kolejnego dealera\n");
					continue;
				}
				System.out.println("-- Ilość stron do przeszukania: " + pages + "\n");

				for (int i = 1; i <= pages; i++) {
					System.out.println("- Strona nr: " + i);
					try {
						xlsPackage.setProducts(getMdt().getOsoboweProducts(dealer, i));
					} catch (OtoMotoXpathException e) {
						System.out.println(e.getMessage());
						System.out.println("-- \nPrzechodze do kolejnej strony\n");
						continue;
					}
					xlsExporter.appendToFile(xlsPackage);
				}
			}
		}
	}

	private void crawlFromStart(String provinceName, List<DealerItem> dealers) throws OtoMotoConnectionException, OtoMotoCrawlerException, ConfigException, OtoMotoDataException {
		for (DealerItem dealer : dealers) {

			XlsPackage xlsPackage = new XlsPackage();
			xlsPackage.setProvince(provinceName);
			xlsPackage.setDealer(dealer);
			System.out.println("-- Strona dealera: " + dealer.getWww());

			int pages;
			try {
				pages = getMdt().getOsoboweDealerPagesCount(dealer.getWww());
			} catch (OtoMotoXpathException e) {
				System.out.println(e.getMessage());
				System.out.println("-- \nPrzechodze do kolejnego dealera\n");
				continue;
			}
			System.out.println("-- Ilość stron do przeszukania: " + pages);

			for (int i = 1; i <= pages; i++) {
				System.out.println("- Strona nr: " + i);
				try {
					xlsPackage.setProducts(getMdt().getOsoboweProducts(dealer, i));
				} catch (OtoMotoXpathException e) {
					System.out.println(e.getMessage());
					System.out.println("-- \nPrzechodze do kolejnej strony\n");
					continue;
				}
				xlsExporter.appendToFile(xlsPackage);
			}
		}
	}

	private void removeLastExportedDealerFromXls(String lastDealerName) throws OtoMotoCrawlerException {
		getOutputXls().removeTrailingRows(lastDealerName);
	}

	private String getLastExportedDealerName() throws OtoMotoCrawlerException {
		return getOutputXls().getLastExportedDealerName();
	}

	private XlsImporter getInputXls() throws OtoMotoCrawlerException {
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
