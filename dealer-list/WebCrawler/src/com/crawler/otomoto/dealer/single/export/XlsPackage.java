package com.crawler.otomoto.dealer.single.export;

import java.util.List;

import com.crawler.otomoto.dealer.list.DealerItem;
import com.crawler.otomoto.dealer.single.ProductItem;

public class XlsPackage {

	private String province;
	private DealerItem dealer;
	private List<ProductItem> products;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public DealerItem getDealer() {
		return dealer;
	}

	public void setDealer(DealerItem dealer) {
		this.dealer = dealer;
	}

	public List<ProductItem> getProducts() {
		return products;
	}

	public void setProducts(List<ProductItem> products) {
		this.products = products;
	}

}
