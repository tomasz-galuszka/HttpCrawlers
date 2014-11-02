package com.crawler.autowereld.export.serialization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.crawler.autowereld.data.beans.CarItem;

public class DealersContainer implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<CarItem> cars;
	private int currentDealerIndexSheet;
	private String sheetName;

	public DealersContainer() {
		cars = new ArrayList<CarItem>();
	}

	public void add(CarItem c) {
		cars.add(c);
	}

	public List<CarItem> getList() {
		return cars;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public int getCurrentDealerIndexSheet() {
		return currentDealerIndexSheet;
	}

	public void setCurrentDealerIndexSheet(int currentDealerIndexSheet) {
		this.currentDealerIndexSheet = currentDealerIndexSheet;
	}

}