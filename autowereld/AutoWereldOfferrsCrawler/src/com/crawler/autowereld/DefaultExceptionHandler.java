package com.crawler.autowereld;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.crawler.autowereld.config.ConfigException;
import com.crawler.autowereld.connection.ConnectionException;
import com.crawler.autowereld.export.CrawlerImportException;

public class DefaultExceptionHandler {

	public static void handleException(Exception e) {
		if (e instanceof java.lang.ArrayIndexOutOfBoundsException) {
			System.out.println("-- Wynierz plik do odczytu.");
			return;
		}

		if (e instanceof ConfigException || e instanceof ConnectionException || e instanceof CrawlerImportException) {
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.println(e.getClass().getCanonicalName());
		System.out.println(e.getMessage());
		e.printStackTrace();
	}

}
