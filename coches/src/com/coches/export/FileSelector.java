package com.coches.export;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileSelector extends JFrame {

	private static final long serialVersionUID = 1L;

	public File selectFileDat() throws FileNotFoundException {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Only dat files", "dat");
		chooser.setFileFilter(filter);
		chooser.showOpenDialog(this);
		if (!chooser.getSelectedFile().exists()) {
			throw new FileNotFoundException("Selected file do not exists\nRun crawler with proper path to file.");
		}
		return chooser.getSelectedFile();
	}
	
	public File selectPathToSave() throws FileNotFoundException {
		JFileChooser chooser = new JFileChooser();
		chooser.showSaveDialog(this);
		if (!chooser.getSelectedFile().getAbsolutePath().endsWith(".xls")) {
			throw new FileNotFoundException("Output file should have xls extenstions. For example mydata.xls.");
		}
		return chooser.getSelectedFile();
	}
}
