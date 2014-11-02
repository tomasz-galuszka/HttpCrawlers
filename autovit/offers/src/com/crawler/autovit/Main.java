package com.crawler.autovit;

public class Main {

	private static String provinceFileXls;

	public static void main(String[] args) {

		try {
			provinceFileXls = args[0];
		} catch (Exception e) {
			System.out.println("\nProgram nalezy uruchomic ze sciezka do pliku hrabstwa np:\n\n java -jar c:\\Constanta.xls\n");
			System.exit(1);
		}

		try {
			Crawler crawler = new Crawler(provinceFileXls);
			crawler.doCrawl();
		} catch (Exception e) {
			MessageBoxExceptionHandler.handle(e);
		}
	}
}