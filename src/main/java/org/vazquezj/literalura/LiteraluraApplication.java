package org.vazquezj.literalura;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.vazquezj.literalura.service.APIFetcher;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Hola mundo");
		APIFetcher fetchAPI = new APIFetcher();
		String response = fetchAPI.obtenerDatos("https://gutendex.com/books?search=pride");
		System.out.println(response);
		System.out.printf("La respuesta tiene %d caracteres", response.length());
	}
}
