package com.coches.data;

import java.util.Arrays;
import java.util.List;


public enum Province {
	
	A_CORUNA("A Coruña","a_coruna"),
	ALAVA("Alava","alava"),
	ALBACETE("Albacete","albacete"),
	ALICANTE("Alicante","alicante"),
	ALMERIA("Almería","almeria"),
	ASTURIAS("Asturias","asturias"),
	AVILA("Ávila","avila"),
	BADAJOZ("Badajoz","badajoz"),
	BALEARES("Baleares","baleares"),
	BARCELONA("Barcelona","barcelona"),
	BURGOS("Burgos","burgos"),
	CACERES("Cáceres","caceres"),
	CADIZ("Cádiz","cadiz"),
	CANTABRIA("Cantabria","cantabria"),
	CASTELLON("Castellón","castellon"),
	CEUTA("Ceuta","ceuta"),
	CIUDAD_REAL("Ciudad Real","ciudad_real"),
	CORDOBA("Córdoba","cordoba"),
	CUENCA("Cuenca","cuenca"),
	GIRONA("Girona","girona"),
	GRANADA("Granada","granada"),
	GUADALAJARA("Guadalajara","guadalajara"),
	GUIPUZCOA("Guipúzcoa","guipuzcoa"),
	HUELVA("Huelva","huelva"),
	HUESCA("Huesca","huesca"),
	JAEN("Jaén","jaen"),
	LA_RIOJA("La Rioja","la_rioja"),
	LAS_PALMAS("Las Palmas","las_palmas"),
	LEON("León","leon"),
	LLEIDA("Lleida","lleida"),
	LUGO("Lugo","lugo"),
	MADRID("Madrid","madrid"),
	MALAGA("Málaga","malaga"),
	MELILLA("Melilla","melilla"),
	MURCIA("Murcia","murcia"),
	NAVARRA("Navarra","navarra"),
	ORENSE("Orense","orense"),
	PALENCIA("Palencia","palencia"),
	PONTEVEDRA("Pontevedra","pontevedra"),
	SALAMANCA("Salamanca","salamanca"),
	SEGOVIA("Segovia","segovia"),
	SEVILLA("Sevilla","sevilla"),
	SORIA("Soria","soria"),
	STA_C_TENERIFE("Sta. C. Tenerife","sta_c_tenerife"),
	TARRAGONA("Tarragona","tarragona"),
	TERUEL("Teruel","teruel"),
	TOLEDO("Toledo","toledo"),
	VALENCIA("Valencia","valencia"),
	VALLADOLID("Valladolid","valladolid"),
	VIZCAYA("Vizcaya","vizcaya"),
	ZAMORA("Zamora","zamora"),
	ZARAGOZA("Zaragoza","zaragoza");

	private String id;
	private String name;
	
	private Province(String name, String id) {
		this.name = name;
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public static List<Province> getProvinces() {
		Province[] values = Province.values();
		List<Province> provinces = Arrays.asList(values);
		return provinces;
	}
}
