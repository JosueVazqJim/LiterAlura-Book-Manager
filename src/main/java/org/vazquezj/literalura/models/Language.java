package org.vazquezj.literalura.models;

public enum Language {
	ENGLISH("en"),
	SPANISH("es"),
	PORTUGUESE("pt"),
	ITALIAN("it"),
	FRENCH("fr"),
	GERMAN("de"),
	RUSSIAN("ru"),
	JAPANESE("ja"),
	CHINESE("zh"),
	ARABIC("ar"),
	TURKISH("tr"),
	POLISH("pl"),
	DUTCH("nl"),
	CZECH("cs");

	private final String code;

	Language(String code) {
		this.code = code;
	}

	public static Language fromCode(String code) {
		for (Language languaje : Language.values()) {
			if (languaje.code.equalsIgnoreCase(code)) {
				return languaje;
			}
		}
		throw new IllegalArgumentException("Invalid code: " + code);
	}
}
