package com.crawler.autovit.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class FilePathPanel extends JPanel {

	private static final long serialVersionUID = 8527004390057114793L;
	private JButton selectFileButton;
	private JLabel selectedFilePathLabel;
	private MyFileChooser fileChooser;
	private File selectedFilePath;

	public FilePathPanel() {
		super();
		Border lineBorder = BorderFactory.createTitledBorder("Scieżka do pliku wynikowego:");
		setBorder(lineBorder);
		setLayout(null);
		initItems();
	}

	private void initItems() {
		initButton();
		initLabel();
	}

	private void initButton() {
		selectFileButton = new JButton("Wybierz");
		selectFileButton.setToolTipText("Wbierz aby zdefiniować miejsce pliku wynikowego.");
		selectFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser = new MyFileChooser();
				int retVal = fileChooser.showSaveDialog(FilePathPanel.this);
				if (retVal == JFileChooser.APPROVE_OPTION) {
					
					selectedFilePath = fileChooser.getSelectedFile();
					selectedFilePathLabel.setText(getSelectedFilePath().getAbsolutePath());
					
					selectedFilePath = new File(getSelectedFilePath() + File.separator + "dane.xls");
					System.out.println(getSelectedFilePath().getAbsolutePath());
				}
			}
		});
		selectFileButton.setBounds(10, 20, 100, 25);
		add(selectFileButton);
	}

	private void initLabel() {
		selectedFilePathLabel = new JLabel("Brak");
		selectedFilePathLabel.setBounds(120, 20, 450, 25);
		add(selectedFilePathLabel);
	}

	public File getSelectedFilePath() {
		return selectedFilePath;
	}

	public void disableUI() {
		selectFileButton.setEnabled(false);
	}
	
	public void enableUI() {
		selectFileButton.setEnabled(true);
	}
}
