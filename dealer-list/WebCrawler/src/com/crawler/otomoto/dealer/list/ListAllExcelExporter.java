package com.crawler.otomoto.dealer.list;

import java.io.File;
import java.io.IOException;
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

public class ListAllExcelExporter {

	public void export(File file, List<DealerItem> dealers, String provinceName) {
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(file);
			WritableSheet sheet = workbook.createSheet(provinceName, 0);

			createHeader(sheet);
			fillWithData(dealers, sheet);
			expandColumns(sheet, 8);

			workbook.write();
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
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
			
			
			String address = dealers.get(i).getAddress();
			
			try {
				String[] splitted = address.split(",");
				String ulica = splitted[0];
				String kodMiasto = splitted[1];
				String kod = kodMiasto.substring(0, 7);
				String miasto = kodMiasto.substring(kod.length(), kodMiasto.length());
				
				sheet.addCell(new Label(4, i + 2, ulica, arial10format));
				sheet.addCell(new Label(5, i + 2, kod, arial10format));
				sheet.addCell(new Label(6, i + 2, miasto, arial10format));
				
			} catch (Exception e) {
				sheet.addCell(new Label(4, i + 2, address, arial10format));
				sheet.addCell(new Label(5, i + 2, "", arial10format));
				sheet.addCell(new Label(6, i + 2, "", arial10format));
			}
			
			sheet.addCell(new Label(7, i + 2, dealers.get(i).getPhone(), arial10format));
			sheet.addCell(new Label(8, i + 2, dealers.get(i).getAuctions(), arial10format));
		}
	}

	private void createHeader(WritableSheet sheet) {
		WritableFont arial14font = new WritableFont(WritableFont.ARIAL, 13, jxl.write.WritableFont.BOLD);
		WritableCellFormat arial14format = new WritableCellFormat(arial14font);
		try {
			arial14format.setBackground(Colour.LIGHT_BLUE);
			arial14format.setBorder(Border.ALL, BorderLineStyle.THIN);
		} catch (WriteException e1) {
			e1.printStackTrace();
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
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}

	private void expandColumns(WritableSheet sheet, int amountOfColumns) {
		int c = amountOfColumns;
		
		CellView cell1 = sheet.getColumnView(1);
		cell1.setSize(7*256);
		sheet.setColumnView(1, cell1);
		
		for (int x = 2; x <= c; x++) {
			
			if (x == 5 || x == amountOfColumns) {
				CellView cell = sheet.getColumnView(x);
				cell.setSize(17*256);
				sheet.setColumnView(x, cell);
				continue;
			}
			
			CellView cell = sheet.getColumnView(x);
			cell.setSize(35*256);
			sheet.setColumnView(x, cell);
		}
	}
}
