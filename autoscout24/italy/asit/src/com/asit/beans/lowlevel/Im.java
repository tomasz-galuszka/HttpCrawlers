package com.asit.beans.lowlevel;

import java.util.Arrays;

public class Im {

	private String ih;
	private String[] il;
	private int ic;

	public String getIh() {
		return ih;
	}

	public void setIh(String ih) {
		this.ih = ih;
	}

	public String[] getIl() {
		return il;
	}

	public void setIl(String[] il) {
		this.il = il;
	}

	public int getIc() {
		return ic;
	}

	public void setIc(int ic) {
		this.ic = ic;
	}

	@Override
	public String toString() {
		return "Im [ih=" + ih + ", il=" + Arrays.toString(il) + ", ic=" + ic + "]";
	}
	
}
