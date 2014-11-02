package com.crawler.otomoto.dealer.list.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.crawler.otomoto.OtoMotoMdt;
import com.crawler.otomoto.dealer.list.DealerItem;
import com.crawler.otomoto.dealer.list.ListAllExcelExporter;
import com.crawler.otomoto.general.MessageBoxExceptionHandler;
import com.crawler.otomoto.general.Province;
import com.crawler.otomoto.general.connection.OtoMotoConnectionException;

public class AppPanel extends JPanel {

	private static final long serialVersionUID = -8482990171875050203L;
	private JComboBox provinceCombo;
	private JLabel provinceLabel;
	private int provinceId = 2;
	private PagesPanel pagesPanel;
	private FilePathPanel filePathPanel;
	private LogPanel logPanel;
	private JButton quitButton;
	private JButton startButton;
	
	private OtoMotoMdt mdt;

	public AppPanel() {
		super();
		setLayout(null);
		initUi();
	}

	public void initUi() {
		initProvinceCombo();
		initPagesPanel();
		initFilePathPanel();
		initLogPanel();
		initButtons();
	}

	private void initProvinceCombo() {
		provinceLabel = new JLabel("Województwo:");
		provinceLabel.setBounds(10, 10, 120, 30);

		provinceCombo = new JComboBox(Province.getNames());
		provinceCombo.setBounds(125, 10, 200, 30);
		provinceCombo.setSelectedIndex(0);
		provinceCombo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				provinceId = provinceCombo.getSelectedIndex() + 2;
				if (provinceId == 16) {
					provinceId +=1;
				} else if (provinceId == 17) {
					provinceId -=1;
				}
			}
		});

		add(provinceLabel);
		add(provinceCombo);
	}

	private void initPagesPanel() {
		pagesPanel = new PagesPanel();
		pagesPanel.setBounds(10, 45, 460, 90);
		add(pagesPanel);
	}

	private void initFilePathPanel() {
		filePathPanel = new FilePathPanel();
		filePathPanel.setBounds(10, 140, 460, 60);
		add(filePathPanel);
	}

	private void initLogPanel() {
		logPanel = new LogPanel();
		logPanel.setBounds(20, 210, 460, 200);
		add(logPanel);
	}

	private void initButtons() {
		startButton = new JButton("Uruchom");
		startButton.setBounds(150, 420, 100, 30);
		startButton.addActionListener(new ActionListener() {

			public void validate() throws OtoMotoConnectionException {
				try {
					Integer from = Integer.valueOf(pagesPanel.getFromField().getText());
					Integer to = Integer.valueOf(pagesPanel.getToField().getText());
					if (from <= 0 || to < from) {
						throw new Exception();
					}
				} catch (Exception e) {
					throw new OtoMotoConnectionException("Niepoprawne parametry stron od do.", e);
				}

				File selectedFilePath = filePathPanel.getSelectedFilePath();
				if (selectedFilePath == null) {
					throw new OtoMotoConnectionException("Brak ścieżki do pliku wyjściowego.");
				}
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					disableUI();
					validate();

					Thread t = new Thread() {
						public void run() {

							Integer from = Integer.valueOf(pagesPanel.getFromField().getText());
							Integer to = Integer.valueOf(pagesPanel.getToField().getText());

							if (from.equals(to)) {
								to += 1;
							}
							File selectedFilePath = filePathPanel.getSelectedFilePath();

							List<DealerItem> result = new ArrayList<DealerItem>();
							try {
								for (int i = from; i < to; i++) {
									List<DealerItem> dealers = getMdt().getDealers(provinceId, i, logPanel);
									result.addAll(dealers);
								}
							} catch (OtoMotoConnectionException e) {
								MessageBoxExceptionHandler.handle(e, AppPanel.this);
								enableUI();
							}

							logPanel.addInfo("- Trwa export danych do pliku ...");
							new ListAllExcelExporter().export(selectedFilePath, result, Province.valueOf(provinceId));
							logPanel.addInfo("- Export zakończony sukcesem");
							enableUI();
						}
					};
					t.start();

				} catch (OtoMotoConnectionException e1) {
					MessageBoxExceptionHandler.handle(e1, AppPanel.this);
					enableUI();
				}
			}
		});
		add(startButton);

		quitButton = new JButton("Zamknij");
		quitButton.setBounds(270, 420, 100, 30);
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		quitButton.setToolTipText("Zakończ działanie programu");
		add(quitButton);
	}

	private OtoMotoMdt getMdt() {
		if (mdt == null) {
			mdt = new OtoMotoMdt();
		}
		return mdt;
	}
	
	private void disableUI() {
		startButton.setEnabled(false);
		provinceCombo.setEnabled(false);
		filePathPanel.disableUI();
		pagesPanel.disableUI();
	}
	
	private void enableUI() {
		startButton.setEnabled(true);
		provinceCombo.setEnabled(true);
		filePathPanel.enableUI();
		pagesPanel.enableUI();
	}
}
