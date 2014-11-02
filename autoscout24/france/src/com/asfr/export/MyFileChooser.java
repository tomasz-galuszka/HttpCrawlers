package com.asfr.export;

import javax.swing.JFileChooser;

public class MyFileChooser extends JFileChooser {

	private static final long serialVersionUID = 4948607029029154957L;

	public MyFileChooser() {
		super();
		setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}
	
}
