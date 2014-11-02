package com.crawler.otomoto.general;

import java.awt.Component;

import javax.swing.JOptionPane;

import com.crawler.otomoto.general.connection.OtoMotoConnectionException;


public class MessageBoxExceptionHandler {
	
	public static void handle(OtoMotoConnectionException ex) {
		System.out.println(ex.getLocalizedMessage());
	}
	
	public static void handle(OtoMotoConnectionException ex, Component c) {
		JOptionPane.showMessageDialog(c, ex.getLocalizedMessage());
	}
}
