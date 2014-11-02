package com.crawler.otomoto.export;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.crawler.otomoto.OtoMotoCrawlerException;
import com.crawler.otomoto.data.beans.DealerItem;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

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

	public XlsImporter(File f) throws OtoMotoCrawlerException {
		this.sheetFile = f;
		try {
			this.workbook = Workbook.getWorkbook(sheetFile);
		} catch (BiffException e) {
			throw new OtoMotoCrawlerException("Nie można otworzyć pliku z lista dealerow  ( "+  f.getAbsolutePath() + " ).\nUruchom program w konsoli i sprawdz logi.", e);
		} catch (IOException e) {
			throw new OtoMotoCrawlerException("Nie można otworzyć pliku z lista dealerow  ( "+  f.getAbsolutePath() + " ).\nUruchom program w konsoli i sprawdz logi.", e);
		}
	}

	public String getSheetName() {
		return this.workbook.getSheetNames()[0];
	}

	public List<DealerItem> geatDealers() {
		List<DealerItem> dealers = new ArrayList<DealerItem>();

		Sheet sheet = workbook.getSheet(0);
		for (int row = START_ROW_POSTION; row < sheet.getRows(); row++) {
			DealerItem item = new DealerItem();

			Cell www = sheet.getCell(WWW_COLUMN_POSITION, row);
			item.setWww(www.getContents());

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
			dealers.add(item);
		}
		workbook.close();
		sort(dealers);
		return dealers;
	}

	private  void sort(List<DealerItem> dealers) {
		Collections.sort(dealers, new Comparator<DealerItem>() {
			@Override
			public int compare(DealerItem o1, DealerItem o2) {
				return o1.getWww().compareTo(o2.getWww());
			}
		});
	}
}
