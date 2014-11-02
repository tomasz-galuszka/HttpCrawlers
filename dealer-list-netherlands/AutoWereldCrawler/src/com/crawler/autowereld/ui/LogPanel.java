package com.crawler.autowereld.ui;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class LogPanel extends JPanel {

	private static final long serialVersionUID = -7935488854474540464L;
	private JTextArea logTextArea;

	public LogPanel() {
		super();
		Border lineBorder = BorderFactory.createTitledBorder("Logi:");
		setBorder(lineBorder);
		setLayout(null);
		initLogArea();
	}

	private void initLogArea() {
		logTextArea = (new JTextArea(70, 260));
		logTextArea.setEditable(false);
		logTextArea.setLineWrap(true);
		logTextArea.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(logTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(10, 20, 440, 170);

		add(scroll);
	}

	
	public void addInfo(String info) {
		logTextArea.append(info + "\n");
		logTextArea.setCaretPosition(logTextArea.getText().length());
	}
}
