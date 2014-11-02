package com.crawler.autowereld;

import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.crawler.autowereld.ui.AppPanel;

public class Main extends JFrame {

	private static final long serialVersionUID = 7641301406000337735L;

	public Main() throws HeadlessException {
		try {
			getContentPane().add(new AppPanel());
			setTitle("Autowereld Crawler v1.3");
			setSize(500, 500);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setResizable(false);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			MessageBoxExceptionHandler.handle(e, Main.this);
		}
	}

	public static void main(String[] args) {
		Main window = new Main();
		window.setVisible(true);
	}
}
