package org.vazquezj.literalura.main;

import org.vazquezj.literalura.models.Book;
import org.vazquezj.literalura.models.DataBook;
import org.vazquezj.literalura.models.DataResponse;
import org.vazquezj.literalura.service.APIFetcher;
import org.vazquezj.literalura.service.DataConverter;

import java.net.URLEncoder;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
	private Scanner scanner = new Scanner(System.in);
	private static APIFetcher fetchAPI = new APIFetcher();
	private DataConverter dataConverter = new DataConverter();
	// Constantes
	private static final String URL_BASE = "https://gutendex.com/books/?search=";

	public void menu() {
		String opcion;
		do {
			mostrarMenu();
			opcion = scanner.nextLine();  // Leemos toda la línea y eliminamos espacios en blanco.

			switch (opcion) {
				case "1":
					buscarLibro();
					break;
				case "0":
					System.out.println("Cerrando la aplicación...");
					break;
				default:
					System.out.println("Opción inválida, por favor intente de nuevo.");
			}
		} while (!opcion.equals("0"));
	}

	private void mostrarMenu() {
		String menu = """
                *************************************************************
                *                                                           *
                *       BIENVENIDO A LA APLICACIÓN DE LITERATURA            *
                *                                                           *
                *************************************************************
                
                1 - Buscar libro (se guarda en la base de datos si no existe aún)
                0 - Salir
                
                Por favor, ingrese el número de la opción deseada: """;
		System.out.println(menu);
	}

	private void buscarLibro() {
		System.out.println("Ingrese el nombre del libro que desea buscar:");
		String tituloLibro = scanner.nextLine();  // Obtenemos el nombre del libro
		if (!tituloLibro.isEmpty()) {
			String json = getDataFromAPI(tituloLibro);  // Obtenemos la respuesta del API
			DataResponse dataResponse = parseResponse(json, DataResponse.class);  // Parseamos la respuesta
			Optional<Book> bookOp = dataResponse.results().stream()
					.findFirst()
					.map(dr -> new Book(dr));

			System.out.printf("Libro encontrado: %s%n", bookOp.get());
		} else {
			System.out.println("No ha ingresado ningún título. Intente de nuevo.");
		}
	}

	private String getDataFromAPI(String search) {
		String encoded = URLEncoder.encode(search, java.nio.charset.StandardCharsets.UTF_8);
		String url = URL_BASE + encoded;
		String json = fetchAPI.obtenerDatos(url);  // Llamamos al API
		return json;
	}

	//metodo para hacer el parseo de la respuesta a un DTO de llegada
	private <T> T parseResponse(String json, Class<T> clase) {
		T dataParser = dataConverter.convertData(json, clase);
		return dataParser;
	}
}
