package com.crawler.autowereld;

import java.io.File;
import java.io.FileNotFoundException;

import com.crawler.autowereld.export.CrawlerExportException;
import com.crawler.autowereld.export.serialization.SerializationException;

public class Main {

	public static void main(String[] args) {
		try {
			checkAndDoExport(args);
			go();
		} catch (Exception e) {
			DefaultExceptionHandler.handleException(e);
		}
		System.exit(0);
	}

	private static void go() throws Exception {
		File inputFile = new FileSelector().selectFileXls();
		Crawler crawler = new Crawler();
		crawler.go(inputFile);
	}

	private static void checkAndDoExport(String[] args) throws FileNotFoundException, CrawlerExportException, SerializationException {
		String arg = "";
		try {
			arg = args[0];
		} catch (Exception e) {
			return;
		}
		if (arg.compareTo("export") == 0) {
			File filePath = new FileSelector().selectFileDat();
			File pathToSave = null;
			try {
				pathToSave = new FileSelector().selectPathToSave();
			} catch (Exception e) {
				DefaultExceptionHandler.handleException(e);
				System.exit(0);
			}
			new Crawler().export(filePath, pathToSave);
			System.exit(0);
		} else {
			System.out.println("Program obsługuje tylko jeden argument:\n export");
		}
	}
}
