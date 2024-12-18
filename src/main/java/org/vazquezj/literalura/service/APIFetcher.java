package org.vazquezj.literalura.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

public class APIFetcher {
	private static final Logger LOGGER = Logger.getLogger(APIFetcher.class.getName());

	public String obtenerDatos(String url) {
		//cliente que se encarga de hacer la peticion
		HttpClient client = HttpClient.newHttpClient();

		//peticion que se va a realizar
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.build();

		//respuesta que se obtiene de la peticion
		HttpResponse<String> response = null;

		//lanzamos la peticion
		try{
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			return response.body();
		} catch (IOException | InterruptedException e) {
			LOGGER.log(Level.SEVERE, "Error fetching data from API", e);
			return null; //
		}
	}
}
