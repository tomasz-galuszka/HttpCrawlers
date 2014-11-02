package com.crawler.otomoto;

import java.awt.Component;

import javax.swing.JOptionPane;

import com.crawler.otomoto.config.ConfigException;

public class MessageBoxExceptionHandler {

	public static void handle(Exception ex, Component c) {
		if (ex instanceof ConfigException) {
			JOptionPane.showMessageDialog(c, ex.getMessage());
			ex.printStackTrace();
			System.exit(-1);
		} else {
			JOptionPane.showMessageDialog(c, ex.getMessage());
			ex.printStackTrace();
		}
	}
}
