package com.crawler.autowereld.export;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.crawler.autowereld.data.beans.DealerItem;
import com.crawler.autowereld.ui.LogPanel;

public class XlsExporter {

	private final int max_sheet_size = 65000;

	public void export(File file, List<DealerItem> dealers, String provinceName, LogPanel logger) throws XlsExporterException {
		int listSize = dealers.size();
		int files = listSize / max_sheet_size;
		int rest = listSize % max_sheet_size;
		
		file = new File(file.getAbsolutePath() + File.separator + "dane.xls");

		if (files == 0 && rest > 0) {
			logger.addInfo(" -- Export 1 pliku");
			exportAsSingleFile(file, dealers, provinceName);

		} else if (files > 0) {
			int start = 0;
			int end = 0;
			for (int i = 1; i <= files; i++) {
				File xlsFile = new File(file.getAbsolutePath());
				xlsFile = new File(file.getAbsolutePath().replace(".xls", "_" + i + "_.xls"));

				logger.addInfo(" -- Export pliku " + i);

				end = i * max_sheet_size;
				exportAsSingleFile(xlsFile, dealers.subList(start, end), provinceName);
				start = end;
			}

			if (rest > 0) {
				logger.addInfo(" -- Export reszty (plik " + (files + 1) + ")");
				File xlsFile = new File(file.getAbsolutePath());
				xlsFile = new File(file.getAbsolutePath().replace(".xls", "_reszta.xls"));
				exportAsSingleFile(xlsFile, dealers.subList(files * max_sheet_size, dealers.size()), provinceName);
			}

		}
	}

	private void exportAsSingleFile(File file, List<DealerItem> dealers, String provinceName) throws XlsExporterException {
		try {
			File xlsFile = new File(file.getAbsolutePath());
			if (xlsFile.exists()) {
				xlsFile = new File(file.getAbsolutePath().replace(".xls", Calendar.getInstance().getTimeInMillis() + ".xls"));
			}
			System.out.println(xlsFile.getAbsolutePath());
			WritableWorkbook workbook = Workbook.createWorkbook(xlsFile);
			WritableSheet sheet = workbook.createSheet(provinceName, 0);

			createHeader(sheet);
			fillWithData(dealers, sheet);
			expandColumns(sheet, 8);

			workbook.write();
			workbook.close();
		} catch (IOException e) {
			throw new XlsExporterException("Wystąpił problem podczas tworzenia pliku. Otwórz program w konsoli i sprawdź logi", e);
		} catch (WriteException e) {
			throw new XlsExporterException("Wystąpił problem podczas zapisu do pliku xls. Otwórz program w konsoli i sprawdź logi", e);
		}
	}

	private void fillWithData(List<DealerItem> dealers, WritableSheet sheet) throws WriteException, RowsExceededException {

		WritableFont arial10font = new WritableFont(WritableFont.ARIAL, 10);
		WritableCellFormat arial10format = new WritableCellFormat(arial10font);
		arial10format.setWrap(true);
		arial10format.setBackground(Colour.GRAY_25);
		arial10format.setBorder(Border.ALL, BorderLineStyle.THIN);

		for (int i = 0; i < dealers.size(); i++) {
			sheet.setRowView(i + 2, 1000);
			sheet.addCell(new Label(1, i + 2, String.valueOf(i + 1), arial10format));
			sheet.addCell(new Label(2, i + 2, dealers.get(i).getWww(), arial10format));
			sheet.addCell(new Label(3, i + 2, dealers.get(i).getName(), arial10format));
			sheet.addCell(new Label(4, i + 2, dealers.get(i).getStreet(), arial10format));
			sheet.addCell(new Label(5, i + 2, dealers.get(i).getZipCode(), arial10format));
			sheet.addCell(new Label(6, i + 2, dealers.get(i).getZipCity(), arial10format));
			sheet.addCell(new Label(7, i + 2, dealers.get(i).getPhone(), arial10format));
			sheet.addCell(new Label(8, i + 2, dealers.get(i).getAuctions(), arial10format));
		}
	}

	private void createHeader(WritableSheet sheet) throws XlsExporterException {
		WritableFont arial14font = new WritableFont(WritableFont.ARIAL, 13, jxl.write.WritableFont.BOLD);
		WritableCellFormat arial14format = new WritableCellFormat(arial14font);
		try {
			arial14format.setBackground(Colour.LIGHT_BLUE);
			arial14format.setBorder(Border.ALL, BorderLineStyle.THIN);
		} catch (WriteException e1) {
			throw new XlsExporterException("Wystąpił problem podczas tworzenia naglowka pliku xls. Otwórz program w konsoli i sprawdź logi", e1);
		}

		try {
			sheet.setRowView(1, 400);
			sheet.addCell(new Label(1, 1, "Lp.", arial14format));
			sheet.addCell(new Label(2, 1, "Strona internetowa", arial14format));
			sheet.addCell(new Label(3, 1, "Nazwa", arial14format));
			sheet.addCell(new Label(4, 1, "Ulica", arial14format));
			sheet.addCell(new Label(5, 1, "Kod Pocztowy", arial14format));
			sheet.addCell(new Label(6, 1, "Miasto", arial14format));
			sheet.addCell(new Label(7, 1, "Telefon", arial14format));
			sheet.addCell(new Label(8, 1, "Ilość aukcji", arial14format));
		} catch (RowsExceededException e) {
			throw new XlsExporterException("Został przekroczony maksymalny rozmiar pliku xls 65536 wierszy", e);
		} catch (WriteException e) {
			throw new XlsExporterException("Wystąpił problem podczas zapisu danych do pliku xls. Otwórz program w konsoli i sprawdź logi", e);
		}
	}

	private void expandColumns(WritableSheet sheet, int amountOfColumns) {
		int c = amountOfColumns;

		CellView cell1 = sheet.getColumnView(1);
		cell1.setSize(7 * 256);
		sheet.setColumnView(1, cell1);

		for (int x = 2; x <= c; x++) {

			if (x == 5 || x == amountOfColumns) {
				CellView cell = sheet.getColumnView(x);
				cell.setSize(17 * 256);
				sheet.setColumnView(x, cell);
				continue;
			}

			CellView cell = sheet.getColumnView(x);
			cell.setSize(35 * 256);
			sheet.setColumnView(x, cell);
		}
	}
}
