package com.coches;

import java.io.File;
import java.io.FileNotFoundException;

import com.coches.crawler.Cars;
import com.coches.crawler.Crawler;
import com.coches.crawler.serialization.CarReader;
import com.coches.crawler.serialization.SerializationException;
import com.coches.data.DataProviderException;
import com.coches.export.CrawlerExportException;
import com.coches.export.FileSelector;
import com.coches.export.XlsExporter;

public class Main {

	public static void main(String[] args) {
		if (args.length == 0) {
			Logger.log("Starting to collect data");

			Crawler crawler = new Crawler();
			try {
				crawler.go();
			} catch (SerializationException e) {
				MessageBoxExceptionHandler.handle(e);
			} catch (DataProviderException e) {
				MessageBoxExceptionHandler.handle(e);
			}

			Logger.log("Collecting data completed !");
			System.exit(-1);
		} else if (args.length == 1) {
			if ("export".equals(args[0])) {
				Logger.log("Beginning to export data");

				try {
					FileSelector fs = new FileSelector();
					File datInFile = fs.selectFileDat();
					File xlsOutFile = fs.selectPathToSave();

					Cars downloadedData = new CarReader().read(datInFile);
					new XlsExporter().createXls(xlsOutFile, downloadedData);
					
				} catch (FileNotFoundException e) {
					MessageBoxExceptionHandler.handle(e);
				} catch (SerializationException e) {
					MessageBoxExceptionHandler.handle(e);
				} catch (CrawlerExportException e) {
					MessageBoxExceptionHandler.handle(e);
				} catch (Exception e) {
					MessageBoxExceptionHandler.handle(e);
				}

				Logger.log("Export data completed !");
				System.exit(-1);
			}
		}

		Logger.log("\nCrawler requires no or one argument\nExample: \n" + "\tjava -jar CochesCrawlerV1.3.jar  -- download data\n"
				+ "\tjava -jar CochesCrawlerV1.3.jar export  -- export downloaded data to xls\n");

	}
}
