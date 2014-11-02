package com.crawler.otomoto.dealer.list.gui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class PagesPanel extends JPanel {

	private static final long serialVersionUID = 5100548690289182326L;
	private JLabel fromLabel;
	private JLabel toLabel;
	private JTextField fromField;
	private JTextField toField;

	public PagesPanel() {
		super();
		Border lineBorder = BorderFactory.createTitledBorder("Strony:");
		setBorder(lineBorder);
		setLayout(null);
		initItems();
	}

	private void initItems() {
		initFromItems();
		initToItems();
	}

	private void initFromItems() {
		fromLabel = new JLabel("Od:");
		fromLabel.setBounds(10, 25, 30, 20);
		
		fromField = new JTextField(5);
		fromField.setBounds(45, 25, 30, 20);
		
		add(fromLabel);
		add(fromField);
	}

	private void initToItems() {
		toLabel = new JLabel("Do:");
		toLabel.setBounds(10, 55, 30, 20);
		
		toField = new JTextField(5);
		toField.setBounds(45, 55, 30, 20);
		
		add(toLabel);
		add(toField);
	}

	public JTextField getFromField() {
		return fromField;
	}

	public JTextField getToField() {
		return toField;
	}

	public void disableUI() {
		fromField.setEnabled(false);
		toField.setEnabled(false);
	}
	
	public void enableUI() {
		fromField.setEnabled(true);
		toField.setEnabled(true);
	}

}
