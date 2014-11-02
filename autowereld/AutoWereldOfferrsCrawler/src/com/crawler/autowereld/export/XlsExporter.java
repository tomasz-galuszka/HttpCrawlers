package com.crawler.autowereld.export;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.crawler.autowereld.data.beans.CarItem;
import com.crawler.autowereld.export.serialization.DealersContainer;

public class XlsExporter {

	private final int max_sheet_size = 60000;

	public void createXls(String filePath, DealersContainer container) throws CrawlerExportException {
		List<CarItem> data = container.getList();
		int listSize = data.size();

		int files = listSize / max_sheet_size;
		int rest = listSize % max_sheet_size;

		if (files == 0 && rest > 0) {
			System.out.println(" -- Export 1 pliku");
			export(filePath, data);
			
		} else if (files > 0) {
			
			int start = 0;
			int end = 0;
			for (int i = 1; i <= files; i++) {
				System.out.println(" -- Export pliku " + i);
				end = i * max_sheet_size;
				export(filePath.replace(".xls", "_" + i + "_.xls"), data.subList(start, end));
				start = end;
			}

			if (rest > 0) {
				System.out.println(" -- Export reszty (plik " + (files + 1) + ")");
				export(filePath.replace(".xls", "_reszta.xls"), data.subList(files * max_sheet_size, data.size()));
			}

		}
	}

	public void export(String filePath, List<CarItem> cars) throws CrawlerExportException {
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(new File(filePath));
			new CarsSheet().export(workbook, cars);
			workbook.write();
			workbook.close();
		} catch (IOException e) {
			throw new CrawlerExportException("Nie można utworzyć pliku xls", e);
		} catch (WriteException e) {
			throw new CrawlerExportException("Nie można utworzyć pliku xls", e);
		}
	}

}
