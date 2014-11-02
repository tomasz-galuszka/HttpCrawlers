package com.crawler.autovit.data;

public enum Province {

	BUCURESTI(68, "BUCURESTI"), ALBA(69, "Alba"), ARAD(70, "Arad"), ARGES(71, "Arges"), BACAU(72, "Bacau"), BIHOR(73, "Bihor"), BISTRITA_NASAUD(74,
			"Bistrita-Nasaud"), BOTOSANI(75, "Botosani"), BRASOV(76, "Brasov"), BRAILA(77, "Braila"), BUZAU(78, "Buzau"), CARASSEVERIN(79, "CarasSeverin"), CALARASI(
			80, "Calarasi"), CLUJ(81, "Cluj"), CONSTANTA(82, "Constanta"), COVASNA(83, "Covasna"), DAMBOVITA(84, "Dambovita"), DOLJ(85, "Dolj"), GALATI(86,
			"Galati"), GIURGIU(87, "Giurgiu"), GORJ(88, "Gorj"), HARGHITA(89, "Harghita"), HUNEDOARA(90, "Hunedoara"), IALOMITA(91, "Ialomita"), IASI(92,
			"Iasi"), ILFOV(93, "Ilfov"), MARAMURES(94, "Maramures"), MEHEDINTI(95, "Mehedinti"), MURES(96, "Mures"), NEAMT(97, "Neamt"), OLT(98, "Olt"), PRAHOVA(
			99, "Prahova"), SATUMARE(100, "SatuMare"), SALAJ(101, "Salaj"), SIBIU(102, "Sibiu"), SUCEAVA(103, "Suceava"), TELEORMAN(104, "Teleorman"), TIMIS(
			105, "Timis"), TULCEA(106, "Tulcea"), VASLUI(107, "Vaslui"), VALCEA(108, "Valcea"), VRANCEA(109, "Vrancea");

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
		String[] result = new String[42];
		int i = 0;
		for (Province p : Province.class.getEnumConstants()) {
			result[i] = p.getName();
			i++;
		}
		return result;
	}

	public static String valueOf(int id) {
		for (Province p : Province.class.getEnumConstants()) {
			if (p.getId() == id) {
				return p.getName();
			}
		}
		return "";
	}
}
