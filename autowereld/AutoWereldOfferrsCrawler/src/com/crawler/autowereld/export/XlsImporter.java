package com.crawler.autowereld.export;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.crawler.autowereld.data.beans.DealerItem;

public class XlsImporter {

	private final int WWW_COLUMN_POSITION = 2;
	private final int NAME_COLUMN_POSITION = 3;
	private final int STREET_COLUMN_POSITION = 4;
	private final int CODE_COLUMN_POSITION = 5;
	private final int CITY_COLUMN_POSITION = 6;
	private final int PHONE_COLUMN_POSITION = 7;
	private final int OFFERT_COUNT_COLUMN_POSITION = 8;

	private final int START_ROW_POSTION = 2;

	private File sheetFile;
	private Workbook workbook;

	public XlsImporter(File f) throws CrawlerImportException {
		this.sheetFile = f;
		try {
			this.workbook = Workbook.getWorkbook(sheetFile);
		} catch (BiffException e) {
			throw new CrawlerImportException("Nie można odczytać pliku xls", e);
		} catch (IOException e) {
			throw new CrawlerImportException("Nie można odczytać pliku xls", e);
		}
	}

	public String getSheetName() {
		return this.workbook.getSheetNames()[0];
	}

	public Map<Integer, DealerItem> readDealers() {
		Map<Integer, DealerItem> dealers = new TreeMap<Integer, DealerItem>();
		Sheet sheet = workbook.getSheet(0);
		for (int row = START_ROW_POSTION; row < sheet.getRows(); row++) {
			DealerItem item = new DealerItem();

			item.setProvince(sheet.getName());

			Cell www = sheet.getCell(WWW_COLUMN_POSITION, row);
			item.setWww(convertDealerWww(www));

			Cell name = sheet.getCell(NAME_COLUMN_POSITION, row);
			item.setName(name.getContents());

			Cell street = sheet.getCell(STREET_COLUMN_POSITION, row);
			Cell code = sheet.getCell(CODE_COLUMN_POSITION, row);
			Cell city = sheet.getCell(CITY_COLUMN_POSITION, row);
			item.setAddress(street.getContents() + ", " + code.getContents() + " " + city.getContents());

			Cell phone = sheet.getCell(PHONE_COLUMN_POSITION, row);
			item.setPhone(phone.getContents());

			Cell offertCount = sheet.getCell(OFFERT_COUNT_COLUMN_POSITION, row);
			item.setAuctions(offertCount.getContents());

			dealers.put(row, item);
		}
		workbook.close();
		return dealers;
	}

	private String convertDealerWww(Cell www) {
		return www.getContents().replace("/details.html", "/auto.html");
	}
}
