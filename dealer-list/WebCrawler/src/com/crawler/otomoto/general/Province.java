package com.crawler.otomoto.general;

public enum Province {

	DOLNOSLASKIE(2, "Dolnośląskie"),
	KUJAWSKO_POMORSKIE(3, "Kujawsko-Pomorskie"),
	LUBELSKIE(4, "Lubelskie"),
	LUBUSKIE(5, "Lubuskie"),
	LODZKIE(6, "Łódzkie"),
	MALOPOLSKIE(7, "Małopolskie"),
	MAZOWIECKIE(8, "Mazowieckie"),
	OPOLSKIE(9, "Opolskie"),
	PODKARPACKIE(10, "Podkarpackie"),
	PODLASKIE(11, "Podlaskie"),
	POMORSKIE(12, "Pomorskie"),
	SLASKIE(13, "Śląskie"),
	SWIETOKRZYSKIE(14, "Świętokrzyskie"),
	WARMINSKO_MAZURSKIE(15, "Warmińsko-Mazurskie"),
	WIELKOPOLSKIE(17, "Wielkopolskie"),
	ZACHODNIO_POMORSKIE(16, "Zachodnio-Pomorskie");
	
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
		String[] result = new String[16];
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
