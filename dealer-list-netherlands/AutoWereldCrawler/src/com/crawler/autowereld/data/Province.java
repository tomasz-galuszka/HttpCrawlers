package com.crawler.autowereld.data;

public enum Province {

	DRENTHE(1, "Drenthe"),
	FLEVOLAND(2, "Flevoland"),
	FRIESLAND(3, "Friesland"),
	GELDERLAND(4, "Gelderland"),
	GRONINGEN(5, "Groningen"),
	LIMBURG(6, "Limburg"),
	NOORD_BRABANT(7, "Noord-Brabant"),
	NOORD_HOLLAND(8, "Noord-Holland"),
	OVERIJSSEL(9, "Overijssel"),
	UTRECHT(10, "Utrecht"),
	ZEELAND(11, "Zeeland"),
	ZUID_HOLLAND(12, "Zuid-Holland");
	
	private int id;
	private String name;
	
	private Province(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static String[] getNames() {
		String[] result = new String[12];
		int i = 0;
		for(Province p : Province.class.getEnumConstants()) {
			result[i] = p.getName();
			i++;
		}
		return result;
	}
	
	public static String valueOf(int id) {
		for(Province p : Province.class.getEnumConstants()) {
			if(p.getId() == id) {
				return p.getName();
			}
		}
		return "";
	}
}
