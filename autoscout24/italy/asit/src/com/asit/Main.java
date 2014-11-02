package com.asit;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.asit.data.DealersContainer;
import com.asit.export.CrawlerExportException;
import com.asit.export.MyFileChooser;
import com.asit.export.XlsExporter;

public class Main extends JFrame {

	private static final long serialVersionUID = -5243322248668893480L;
	private String selectedFilePath;

	public static void main(String[] args) {
		Crawler crawler = new Crawler();
		try {
			for (String string : args) {
				System.out.println(string);
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
			System.out.println("Program przyjmuje tylko jeden argument: \n export - eksportuje pobrane dane do pliku xls");
			System.exit(1);
		}

		Main app = new Main();
		app.showFileDialog();
		
		if (app.getSelectedFilePath() == null) {
			System.out.println("Nie wybrano pliku.");
			System.exit(1);
			
		}
		System.out.println(app.getSelectedFilePath());

		if (!app.getSelectedFilePath().contains(".xls")) {
			System.out.println("Wybrany plik powinien mieć rozszerzenie *.xls np. C:\\Holandia.xls");
			System.exit(1);
		}
		
		Crawler crawler = new Crawler();
		crawler.deserialize();
		
		try {
			DealersContainer dataContainer = crawler.getDataContainer();
			if (dataContainer == null) {
				System.out.println("Crawler nie posiada pobranych danych.\nAby pobrać dane uruchom program bez argumentu export");
				return;
			}
			new XlsExporter().createXls(app.getSelectedFilePath(), dataContainer);
		} catch (CrawlerExportException e) {
			System.out.println(e.getMessage());
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
