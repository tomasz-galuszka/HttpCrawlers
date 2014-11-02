package com.asfr;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.asfr.data.DealersContainer;
import com.asfr.export.CrawlerExportException;
import com.asfr.export.MyFileChooser;
import com.asfr.export.XlsExporter;

public class Main extends JFrame {

	private static final long serialVersionUID = -5243322248668893480L;
	private String selectedFilePath;

	public static void main(String[] args) {
		Crawler crawler = new Crawler();
		try {
			for (String string : args) {
				Logger.log(string);
			}
			String arg = args[0];
			exportToXls(arg);
			System.exit(0);
		} catch(ArrayIndexOutOfBoundsException e) {
			crawler.go();
		} catch (Exception e) {
			System.exit(0);
		}
		crawler.go();
	}

	private static void exportToXls(String arg) {
		if (!arg.equalsIgnoreCase("export")) {
			Logger.log("Program przyjmuje tylko jeden argument: \n export - eksportuje pobrane dane do pliku xls");
			System.exit(1);
		}

		Main app = new Main();
		app.showFileDialog();
		
		if (app.getSelectedFilePath() == null) {
			Logger.log("Nie wybrano pliku.");
			System.exit(1);
			
		}
		Logger.log(app.getSelectedFilePath());

		if (!app.getSelectedFilePath().contains(".xls")) {
			Logger.log("Wybrany plik powinien mieć rozszerzenie *.xls np. C:\\Holandia.xls");
			System.exit(1);
		}
		
		Crawler crawler = new Crawler();
		crawler.deserialize();
		
		try {
			DealersContainer dataContainer = crawler.getDataContainer();
			if (dataContainer == null) {
				Logger.log("Crawler nie posiada pobranych danych.\nAby pobrać dane uruchom program bez argumentu export");
				return;
			}
			new XlsExporter().createXls(app.getSelectedFilePath(), dataContainer);
		} catch (CrawlerExportException e) {
			Logger.log(e.getMessage());
		}
	}

	public void showFileDialog() {
		MyFileChooser fileChooser = new MyFileChooser();
		int retVal = fileChooser.showSaveDialog(this);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			setSelectedFilePath(fileChooser.getSelectedFile().getAbsolutePath());
		}
	}

	public String getSelectedFilePath() {
		return selectedFilePath;
	}

	public void setSelectedFilePath(String selectedFilePath) {
		this.selectedFilePath = selectedFilePath;
	}
}
