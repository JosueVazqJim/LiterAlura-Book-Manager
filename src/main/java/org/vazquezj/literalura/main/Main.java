package org.vazquezj.literalura.main;

import org.vazquezj.literalura.service.APIFetcher;

public class Main {
	private APIFetcher fetchAPI = new APIFetcher();

	public void menu() {
		var json = fetchAPI.obtenerDatos("https://gutendex.com/books/?search=pride");
		System.out.printf("JSON: %s\n", json);
	}
}