package org.vazquezj.literalura.main;

import org.vazquezj.literalura.service.APIFetcher;

import java.net.URLEncoder;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
	private Scanner scanner = new Scanner(System.in);
	private APIFetcher fetchAPI = new APIFetcher();

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
			// Codificamos el nombre del libro para usarlo en la URL
			String encoded = URLEncoder.encode(tituloLibro, java.nio.charset.StandardCharsets.UTF_8);
			String url = URL_BASE + encoded;
			String json = fetchAPI.obtenerDatos(url);  // Llamamos al API
			System.out.println(json);  // Mostramos la respuesta
		} else {
			System.out.println("No ha ingresado ningún título. Intente de nuevo.");
		}
	}
}
