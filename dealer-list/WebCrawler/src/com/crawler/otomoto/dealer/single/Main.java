package com.crawler.otomoto.dealer.single;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.crawler.otomoto.OtoMotoMdt;
import com.crawler.otomoto.dealer.list.DealerItem;
import com.crawler.otomoto.dealer.list.ListAllExcelImporter;
import com.crawler.otomoto.dealer.single.export.DealerProductsXls;
import com.crawler.otomoto.dealer.single.export.XlsPackage;
import com.crawler.otomoto.general.connection.OtoMotoConnectionException;

public class Main {

	public static void main(String[] args) {

		ListAllExcelImporter excelLoader = new ListAllExcelImporter(new File("Dolnoslaskie.xls"));
		final String provinceName = excelLoader.getSheetName();
		List<DealerItem> dealers = excelLoader.geatDealers();

		sort(dealers);

		OtoMotoMdt mdt = new OtoMotoMdt();
		XlsPackage xlsPackage = new XlsPackage();

		xlsPackage.setProvince(provinceName);

		DealerProductsXls xlsExporter = new DealerProductsXls();
		String lastDealerName = xlsExporter.getLastExportedDealerName();

		xlsExporter.removeTrailingRows(lastDealerName);
		try {
			if (lastDealerName == null) {
				// jedziem całość
				int i = 0;
				for (DealerItem dealerItem : dealers) {
					xlsPackage.setDealer(dealerItem);
					if (i < 3) { // tylko pierwszych dwoch dealerow
						List<ProductItem> productsFirstPage = mdt.getOsoboweProducts(dealerItem, 1); // str
																										// 1
						xlsPackage.setProducts(productsFirstPage);
						xlsExporter.appendToFile(xlsPackage);
					} else {
						System.out.println("Koniec i=" + i);
						break;
					}
					i++;
				}

			} else {
				// jedziem od lastDealer

				boolean start = false;
				int i = 0;
				for (DealerItem dealerItem : dealers) {
					
					if (!start) {
						if (dealerItem.getName().compareTo(lastDealerName) == 0) {
							System.out.println("Jedziem od dealera: " + dealerItem.getName());
							start = true;
						}
					} else {
						xlsPackage.setDealer(dealerItem);
						if (i < 3) { // tylko pierwszych dwoch dealerow
							List<ProductItem> productsFirstPage;

							productsFirstPage = mdt.getOsoboweProducts(dealerItem, 1);// str
																						// 1
							xlsPackage.setProducts(productsFirstPage);
							xlsExporter.appendToFile(xlsPackage);
						} else {
							System.out.println("Koniec i=" + i);
							break;
						}
						i++;
					}
					
				}
			}
		} catch (OtoMotoConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void sort(List<DealerItem> dealers) {
		Collections.sort(dealers, new Comparator<DealerItem>() {
			@Override
			public int compare(DealerItem o1, DealerItem o2) {
				return o1.getWww().compareTo(o2.getWww());
			}
		});
	}
}
