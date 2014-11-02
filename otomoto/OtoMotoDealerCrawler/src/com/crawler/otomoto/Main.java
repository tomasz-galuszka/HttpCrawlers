package com.crawler.otomoto;

import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.crawler.otomoto.ui.AppPanel;

public class Main extends JFrame {

	private static final long serialVersionUID = 7641301406000337735L;

	public Main() throws HeadlessException {
		getContentPane().add(new AppPanel());
		setTitle("OTO-MOTO Crawler v1.3");
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		try {
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		} catch (ClassNotFoundException e) {
			MessageBoxExceptionHandler.handle(e, Main.this);
		} catch (InstantiationException e) {
			MessageBoxExceptionHandler.handle(e, Main.this);
		} catch (IllegalAccessException e) {
			MessageBoxExceptionHandler.handle(e, Main.this);
		} catch (UnsupportedLookAndFeelException e) {
			MessageBoxExceptionHandler.handle(e, Main.this);
		}
	}

	public static void main(String[] args) {
		Main window = new Main();
		window.setVisible(true);
	}
}
