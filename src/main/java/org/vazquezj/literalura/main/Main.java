package org.vazquezj.literalura.main;

import org.vazquezj.literalura.models.Author;
import org.vazquezj.literalura.models.Book;
import org.vazquezj.literalura.models.DataResponse;
import org.vazquezj.literalura.repository.BookRepository;
import org.vazquezj.literalura.service.APIFetcher;
import org.vazquezj.literalura.service.DataConverter;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
	private Scanner scanner = new Scanner(System.in);
	private static APIFetcher fetchAPI = new APIFetcher();
	private DataConverter dataConverter = new DataConverter();
	// Constantes
	private static final String URL_BASE = "https://gutendex.com/books/?search=";
	//dependencias
	private BookRepository repository;
	//variables para guardar libros y autores buscados
	private List<Book> books;
	private List<Author> authors;

	public Main(BookRepository repository) {
		this.repository = repository;
		this.books = new ArrayList<>();
		this.authors = new ArrayList<>();
	}

	public void menu() {
		String opcion;
		do {
			mostrarMenu();
			opcion = scanner.nextLine();  // Leemos toda la línea y eliminamos espacios en blanco.

			switch (opcion) {
				case "1":
					buscarLibro();
					break;
				case "2":
					mostrarLibrosDB();
					break;
				case "3":
					mostrarAutoresDB();
					break;
				case "4":
					mostrarAutoresXAnio();
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
                2 - Mostrar libros guardados en la base de datos
                3 - Mostrar autores guardados en la base de datos
                4 - Mostrar autores vivos en un año específico
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

			System.out.printf(bookOp.get().toString());
			// Guardamos el libro en la base de datos
			Book book = bookOp.get();

			books.add(book);
			authors.addAll(book.getAuthors());

			repository.save(book);
			System.out.printf("\nLibro guardado en la base de datos: %s%n", book.getTitle());
		} else {
			System.out.println("No ha ingresado ningún título. Intente de nuevo.");
		}
	}

	private void mostrarLibrosDB() {
		System.out.println("Libros guardados en la base de datos:");
		List<Book> books = repository.findAll();
		if (books.isEmpty()) {
			System.out.println("No hay libros guardados en la base de datos.");
		} else {
			books.forEach(System.out::println);
		}
	}

	private void mostrarAutoresDB() {
		System.out.println("Autores guardados en la base de datos:");
		List<Author> authors = repository.findAllAuthors();
		if (authors.isEmpty()) {
			System.out.println("No hay autores guardados en la base de datos.");
		} else {
			authors.forEach(System.out::println);
		}
	}

	private void mostrarAutoresXAnio() {
		System.out.println("Ingrese el año del que desea ver los autores vivos:");
		String anio = scanner.nextLine();
		try {
			int year = Integer.parseInt(anio);
			List<Author> authors = repository.findAuthorsByYear(year);
			if (authors.isEmpty()) {
				System.out.printf("No hay autores vivos en el año %d.%n", year);
			} else {
				System.out.printf("Autores vivos en el año %d:%n", year);
				authors.forEach(System.out::println);
			}
		} catch (NumberFormatException e) {
			System.out.println("El valor ingresado no es un año válido. Por favor, ingrese un número.");
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
