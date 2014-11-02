package com.crawler.autowereld;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileSelector extends JFrame {

	private static final long serialVersionUID = 1L;

	public File selectFileXls() throws FileNotFoundException {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Tylko pliki xls", "xls", "xlsx");
		chooser.setFileFilter(filter);
		chooser.showOpenDialog(this);
		if (!chooser.getSelectedFile().exists()) {
			throw new FileNotFoundException("Wybrany plik nie istnieje\nUruchom program z poprawna sciezka do pliku.");
		}
		return chooser.getSelectedFile();
	}
	
	public File selectFileDat() throws FileNotFoundException {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Tylko pliki dat", "dat");
		chooser.setFileFilter(filter);
		chooser.showOpenDialog(this);
		if (!chooser.getSelectedFile().exists()) {
			throw new FileNotFoundException("Wybrany plik nie istnieje\nUruchom program z poprawna sciezka do pliku.");
		}
		return chooser.getSelectedFile();
	}
	
	public File selectPathToSave() throws FileNotFoundException {
		JFileChooser chooser = new JFileChooser();
		chooser.showSaveDialog(this);
		if (!chooser.getSelectedFile().getAbsolutePath().endsWith(".xls")) {
			throw new FileNotFoundException("Plik wynikowy powinien mieć rozszerzenie .xls. Np dane.xls.");
		}
		return chooser.getSelectedFile();
	}
}
