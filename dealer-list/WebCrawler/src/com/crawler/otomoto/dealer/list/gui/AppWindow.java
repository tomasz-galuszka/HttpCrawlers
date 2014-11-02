package com.crawler.otomoto.dealer.list.gui;

import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class AppWindow extends JFrame {

	private static final long serialVersionUID = 7641301406000337735L;

	public AppWindow() throws HeadlessException {
		getContentPane().add(new AppPanel());
		setTitle("OTO-MOTO Crawler v1.2");
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		try {
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		AppWindow window = new AppWindow();
		window.setVisible(true);
	}

}
