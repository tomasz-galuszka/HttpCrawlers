package com.crawler.otomoto.dealer.single.export;

import java.io.File;

public class DirectoryManager {
	
	private final String rootDir = "baza";

	public void createDir(String province, String dealerWWW) {
		String dealerDir = dealerWWW.replace("http://", "");
		File root = new File(rootDir);
		if (!root.exists()) {
			root.mkdir();
		}
		
		File dir = new File(rootDir + File.separator + province + File.separator + dealerDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
}
