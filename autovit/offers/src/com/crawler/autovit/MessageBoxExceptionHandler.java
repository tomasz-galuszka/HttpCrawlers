package com.crawler.autovit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.crawler.autovit.config.ConfigException;
import com.crawler.autovit.connection.ConnectionException;

public class MessageBoxExceptionHandler {

	public static void handle(Exception ex) {
		if (ex instanceof ConfigException) {
			JOptionPane.showMessageDialog(new JFrame(), ex.getMessage());
			ex.printStackTrace();
			System.exit(-1);
		} else if (ex instanceof ConnectionException || ex instanceof CrawlerException) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
			ex.printStackTrace();
		} else {
			JOptionPane.showMessageDialog(new JFrame(), ex.getMessage());
			ex.printStackTrace();
			System.exit(-1);
		}
	}
	
}
