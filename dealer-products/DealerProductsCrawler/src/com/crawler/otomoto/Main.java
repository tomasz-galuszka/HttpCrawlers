package com.crawler.otomoto;

import javax.swing.JFrame;

public class Main {

	private static String provinceFileXls;

	public static void main(String[] args) {

		try {
			provinceFileXls = args[0];
		} catch (Exception e) {
			System.out.println("\nProgram nalezy uruchomic ze sciezka do pliku wojewodztwa np:\n\n java -jar c:\\Dolnoslaskie.xls\n");
			System.exit(1);
		}

		try {
			OtoMotoCrawler crawler = new OtoMotoCrawler(provinceFileXls);
			crawler.doCrawl();
		} catch (Exception e) {
			MessageBoxExceptionHandler.handle(e, new JFrame());
		}
	}
}
