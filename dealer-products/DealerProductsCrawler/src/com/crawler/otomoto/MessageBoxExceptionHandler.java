package com.crawler.otomoto;

import java.awt.Component;

import javax.swing.JOptionPane;

import com.crawler.otomoto.config.ConfigException;
import com.crawler.otomoto.connection.OtoMotoConnectionException;

public class MessageBoxExceptionHandler {

	public static void handle(Exception ex, Component c) {
		if (ex instanceof ConfigException) {
			JOptionPane.showMessageDialog(c, ex.getMessage());
			ex.printStackTrace();
			System.exit(-1);
		} else if (ex instanceof OtoMotoConnectionException || ex instanceof OtoMotoCrawlerException || ex instanceof OtoMotoCrawlerException) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
			ex.printStackTrace();
		} else {
			JOptionPane.showMessageDialog(c, ex.getMessage());
			ex.printStackTrace();
			System.exit(-1);
		}
	}
}
