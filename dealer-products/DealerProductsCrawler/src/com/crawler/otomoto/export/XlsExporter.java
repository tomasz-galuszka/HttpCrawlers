package com.crawler.otomoto.export;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.crawler.otomoto.OtoMotoCrawlerException;
import com.crawler.otomoto.data.beans.ProductItem;

import jxl.Cell;
import jxl.CellView;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class XlsExporter {

	private static final String TMP_XLS = "tmp.xls";
	private String outputXls = "polska.xls";
	private final String ROOT_DIR = "baza";
	private final String LP_COLUMN_NAME = "Lp.";
	private final String PROVINCE_COLUMN_NAME = "Województwo";
	private final String DEALER_NAME = "Nazwa dealera";
	private final String MODEL_COLUMN_NAME = "Model,Typ";
	private final String YEAR_COLUMN_NAME = "Rocznik";
	private final String PRZEBIEG_COLUMN_NAME = "Przebieg";
	private final String PRICE_COLUMN_NAME = "Cena";
	
	public XlsExporter(String outputXls) {
		this.outputXls = outputXls;
	}

	public boolean isFilePresent(File f) {
		try {
			Workbook workbook = Workbook.getWorkbook(f);
			workbook.close();
		} catch (BiffException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public void createXls(boolean fileExist) throws OtoMotoCrawlerException {
		try {
			File destFile = getDestFilePath();
			if (!fileExist) {
				createNewXls(destFile);
			}
		} catch (IOException e) {
			throw new OtoMotoCrawlerException("Nie można utworzyć wynikowego pliku xls", e);
		} catch (WriteException e) {
			throw new OtoMotoCrawlerException("Nie można utworzyć wynikowego pliku xls", e);
		}
	}

	private WritableWorkbook createNewXls(File destFile) throws IOException, WriteException {
		WritableWorkbook workbook = Workbook.createWorkbook(destFile);
		return workbook;
	}

	private void renameCopyToOriginal(File destFile, File copyDestFile) {
		copyDestFile.renameTo(destFile);
	}

	private void deleteOriginalFile(File destFile) {
		destFile.delete();
	}

	private WritableWorkbook createXlsCopy(File destFile, File copyDestFile) throws IOException, BiffException, WriteException {
		Workbook workbook = Workbook.getWorkbook(destFile);
		WritableWorkbook copy = Workbook.createWorkbook(copyDestFile, workbook);

		workbook.close();
		return copy;
	}

	public void appendToFile(XlsPackage xlsPackage) throws OtoMotoCrawlerException {
		File destFile = getDestFilePath();
		File copyDestFile = getCopyDestFilePath();
		try {
			if (!isFilePresent(destFile)) {
				WritableWorkbook workbook = createNewXls(destFile);
				WritableSheet sheet = workbook.createSheet(ROOT_DIR, 0);
				createHeader(sheet);

				// do modifications
				int lastRow = sheet.getRows();

				// fil data from last row
				fillWithData(xlsPackage, sheet, lastRow);

				expandColumns(sheet, 7);
				workbook.write();
				workbook.close();

			} else {
				WritableWorkbook workbook = createXlsCopy(destFile, copyDestFile);
				WritableSheet sheet = workbook.getSheet(0);

				// do modifications
				int lastRow = sheet.getRows();

				// fil data from last row
				fillWithData(xlsPackage, sheet, lastRow);

				expandColumns(sheet, 7);
				workbook.write();
				workbook.close();

				updateXls();
			}

		} catch (BiffException e) {
			throw new OtoMotoCrawlerException("Nie mozna odczytac wynikowego pliku xls", e);
		} catch (WriteException e) {
			throw new OtoMotoCrawlerException("Nie mozna zapisac do wynikowego pliku xls", e);
		} catch (IOException e) {
			throw new OtoMotoCrawlerException("Problem ze znalezieniem pliku xls", e);
		}

	}

	private void createHeader(WritableSheet sheet) throws OtoMotoCrawlerException {
		WritableFont arial14font = new WritableFont(WritableFont.ARIAL, 13, jxl.write.WritableFont.BOLD);
		WritableCellFormat arial14format = new WritableCellFormat(arial14font);
		try {
			arial14format.setBackground(Colour.LIGHT_GREEN);
			arial14format.setBorder(Border.ALL, BorderLineStyle.THIN);
		} catch (WriteException e1) {
			throw new OtoMotoCrawlerException("Blad podczas tworzenia naglowka tabeli w pliku xls", e1);
		}
		try {
			sheet.setRowView(1, 400);
			sheet.addCell(new Label(1, 1, LP_COLUMN_NAME, arial14format));
			sheet.addCell(new Label(2, 1, PROVINCE_COLUMN_NAME, arial14format));
			sheet.addCell(new Label(3, 1, DEALER_NAME, arial14format));
			sheet.addCell(new Label(4, 1, MODEL_COLUMN_NAME, arial14format));
			sheet.addCell(new Label(5, 1, YEAR_COLUMN_NAME, arial14format));
			sheet.addCell(new Label(6, 1, PRZEBIEG_COLUMN_NAME, arial14format));
			sheet.addCell(new Label(7, 1, PRICE_COLUMN_NAME, arial14format));
		} catch (RowsExceededException e) {
			throw new OtoMotoCrawlerException("Blad podczas tworzenia nagloka tabeli w pliku xls, przekroczony limit wierszy", e);
		} catch (WriteException e) {
			throw new OtoMotoCrawlerException("Blad podczas tworzenia naglowka tabeli w pliku xls, problem z zapisem", e);
		}
	}

	private void fillWithData(XlsPackage xlsPackage, WritableSheet sheet, int startRow) throws WriteException, RowsExceededException {

		WritableFont arial10font = new WritableFont(WritableFont.ARIAL, 10);
		WritableCellFormat arial10format = new WritableCellFormat(arial10font);
		arial10format.setWrap(true);
		arial10format.setBackground(Colour.GRAY_25);
		arial10format.setBorder(Border.ALL, BorderLineStyle.THIN);

		List<ProductItem> products = xlsPackage.getProducts();
		int i = startRow;
		for (ProductItem p : products) {
			sheet.setRowView(i, 1000);
			sheet.addCell(new Label(1, i, String.valueOf(i + 1), arial10format));
			sheet.addCell(new Label(2, i, xlsPackage.getProvince(), arial10format));
			sheet.addCell(new Label(3, i, xlsPackage.getDealer().getName(), arial10format));
			sheet.addCell(new Label(4, i, p.getModelType(), arial10format));
			sheet.addCell(new Label(5, i, p.getYear(), arial10format));
			sheet.addCell(new Label(6, i, p.getPrzebieg(), arial10format));
			sheet.addCell(new Label(7, i, p.getPrice() + " " + p.getPriceCurrency().trim(), arial10format));
			i++;
		}
		System.out.println("-- Wypelniam xls ,ilosc:" + products.size());
	}

	private File getDestFilePath() {
		File rootDir = new File(ROOT_DIR);
		if (!rootDir.exists()) {
			rootDir.mkdir();
		}
		File destFile = new File(rootDir.getAbsolutePath() + File.separator + outputXls);
		return destFile;
	}

	private File getCopyDestFilePath() {
		File rootDir = new File(ROOT_DIR);
		if (!rootDir.exists()) {
			rootDir.mkdir();
		}
		File destFile = new File(rootDir.getAbsolutePath() + File.separator + TMP_XLS);
		return destFile;
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

	public String getLastExportedDealerName() throws OtoMotoCrawlerException {
		if (!isFilePresent(getDestFilePath())) {
			return null;
		}
		try {
			Workbook workbook = Workbook.getWorkbook(getDestFilePath());
			Sheet sheet = workbook.getSheet(0);
			Cell[] lastRow = sheet.getRow(sheet.getRows() - 1);
			workbook.close();

			if (lastRow.length == 0) {
				return null;
			}
			return lastRow[3].getContents();
		} catch (BiffException e) {
			throw new OtoMotoCrawlerException("Nie mozna odczytac wynikowego pliku xls", e);
		} catch (IOException e) {
			throw new OtoMotoCrawlerException("Problem ze znalezieniem pliku xls", e);
		}
	}
	
	public String getLastExportedDealerProvince() throws OtoMotoCrawlerException {
		if (!isFilePresent(getDestFilePath())) {
			return null;
		}
		try {
			Workbook workbook = Workbook.getWorkbook(getDestFilePath());
			Sheet sheet = workbook.getSheet(0);
			Cell[] lastRow = sheet.getRow(sheet.getRows() - 1);
			workbook.close();

			if (lastRow.length == 0) {
				return null;
			}
			return lastRow[2].getContents();
		} catch (BiffException e) {
			throw new OtoMotoCrawlerException("Nie mozna odczytac wynikowego pliku xls", e);
		} catch (IOException e) {
			throw new OtoMotoCrawlerException("Problem ze znalezieniem pliku xls", e);
		}
	}

	public void removeTrailingRows(String lastDealerName) throws OtoMotoCrawlerException {
		if (lastDealerName == null  || lastDealerName.compareTo(DEALER_NAME) == 0) {
			return;
		}

		try {
			// find first row where lastDealerName
			WritableWorkbook workbook = createXlsCopy(getDestFilePath(), getCopyDestFilePath());
			WritableSheet sheet = workbook.getSheet(0);
			
			// delete from this row to end
			Cell lastDealerCell = sheet.findCell(lastDealerName);
			if (lastDealerCell == null) {
				workbook.write();
				workbook.close();
				return;
			}
			
			int startRow = lastDealerCell.getRow();
			int endRow = sheet.getRows();
			
			for (int i = startRow; i < endRow; i++) {
				sheet.removeRow(i);
			}
			
			workbook.write();
			workbook.close();
			
			System.out.println("Aktualizuje plik xls");
			updateXls();
			
		} catch (BiffException e) {
			throw new OtoMotoCrawlerException("Usuwanie nieukonczonego dealera z listy. Nie mozna odczytac wynikowego pliku xls", e);
		} catch (WriteException e) {
			throw new OtoMotoCrawlerException("Usuwanie nieukonczonego dealera z listy. Nie mozna zapisac do wynikowego pliku xls", e);
		} catch (IOException e) {
			throw new OtoMotoCrawlerException("Usuwanie nieukonczonego dealera z listy. Nie mozna znalezc pliku wynikowego pliku xls", e);
		}
	}

	private void updateXls() {
		deleteOriginalFile(getDestFilePath());
		renameCopyToOriginal(getDestFilePath(), getCopyDestFilePath());
	}
}