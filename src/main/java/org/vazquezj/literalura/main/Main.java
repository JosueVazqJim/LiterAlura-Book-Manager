package org.vazquezj.literalura.main;

import org.vazquezj.literalura.models.Author;
import org.vazquezj.literalura.models.Book;
import org.vazquezj.literalura.models.DataResponse;
import org.vazquezj.literalura.models.Language;
import org.vazquezj.literalura.repository.AuthorRepository;
import org.vazquezj.literalura.service.APIFetcher;
import org.vazquezj.literalura.service.DataConverter;

import java.net.URLEncoder;
import java.util.*;

public class Main {
	private Scanner scanner = new Scanner(System.in);
	private static APIFetcher fetchAPI = new APIFetcher();
	private DataConverter dataConverter = new DataConverter();
	private static final String URL_BASE = "https://gutendex.com/books/?search=";
	private AuthorRepository repository;

	public Main(AuthorRepository repository) {
		this.repository = repository;
	}

	public void menu() {
		String opcion;
		do {
			mostrarMenu();
			opcion = scanner.nextLine();  // Leemos toda la línea y eliminamos espacios en blanco.
			manejarOpcion(opcion);
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
                5 - Mostrar libros por idioma
                6 - Ver estadisticas de libros
                0 - Salir
                Por favor, ingrese el número de la opción deseada:""";
		System.out.println(menu);
	}

	private void manejarOpcion(String opcion) {
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
			case "5":
				mostrarLibrosXIdioma();
				break;
			case "6":
				mostrarEstadisticas();
				break;
			case "0":
				System.out.println("Cerrando la aplicación...");
				break;
			default:
				System.out.println("Opción inválida, por favor intente de nuevo.");
		}
	}

	private void buscarLibro() {
		System.out.println("Ingrese el nombre del libro que desea buscar:");
		String tituloLibro = scanner.nextLine();
		if (!tituloLibro.isEmpty()) {
			String json = obtenerDatosDeAPI(tituloLibro);
			DataResponse dataResponse = parsearRespuesta(json, DataResponse.class);
			Optional<Book> bookOp = extraerPrimerLibro(dataResponse);
			Optional<Author> authorOp = extraerPrimerAutor(dataResponse);

			if (bookOp.isPresent() && authorOp.isPresent()) {
				Book book = bookOp.get();
				Author author = authorOp.get();
				asociarLibroYAutor(book, author);
			} else {
				System.out.println("No se encontró ningún libro con ese título.");
			}
		} else {
			System.out.println("No ha ingresado ningún título. Intente de nuevo.");
		}
	}

	private String obtenerDatosDeAPI(String search) {
		String encoded = URLEncoder.encode(search, java.nio.charset.StandardCharsets.UTF_8);
		String url = URL_BASE + encoded;
		return fetchAPI.obtenerDatos(url);  // Llamamos al API
	}

	private <T> T parsearRespuesta(String json, Class<T> clase) {
		return dataConverter.convertData(json, clase);
	}

	private Optional<Book> extraerPrimerLibro(DataResponse dataResponse) {
		return dataResponse.results().stream()
				.findFirst()
				.map(Book::new);
	}

	private Optional<Author> extraerPrimerAutor(DataResponse dataResponse) {
		return dataResponse.results().stream()
				.findFirst()
				.map(dr -> {
					String authorName = dr.authors().get(0).name();
					return repository.findByName(authorName)
							.orElseGet(() -> new Author(dr.authors().get(0)));
				});
	}

	private void asociarLibroYAutor(Book book, Author author) {
		book.setAuthor(author);
		boolean bookExists = author.getBooks().stream()
				.anyMatch(b -> b.getTitle().equals(book.getTitle()));

		if (!bookExists) {
			author.getBooks().add(book);
			repository.save(author);
			System.out.println(book);
			System.out.println("Libro encontrado y guardado en la base de datos.");
		} else {
			System.out.println("El libro ya existe en la base de datos.");
		}
	}

	private void mostrarLibrosDB() {
		System.out.println("Libros guardados en la base de datos:");
		List<Book> books = repository.findAllBooks();
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

	private void mostrarLibrosXIdioma() {
		int op = -1;
		Language.showLanguages();
		do {
			System.out.println("Seleccione el idioma del que desea ver los libros:");
			op = scanner.nextInt();
			scanner.nextLine(); // Consume the newline character
		} while (op < 0 || op > Language.values().length);
		Language language = Language.values()[op - 1];
		List<Book> books = repository.findBooksByLanguage(language);
		if (books.isEmpty()) {
			System.out.println("No hay libros guardados en la base de datos del idioma " + language);
		} else {
			System.out.println("Libros guardados en la base de datos del idioma " + language);
			books.forEach(System.out::println);
		}
	}

	private void mostrarEstadisticas() {
		// Mostrar el número de libros y autores
		long numLibros = repository.countBooks();
		long numAutores = repository.countAuthors();

		System.out.println("Estadísticas generales:");
		System.out.println("Número de libros registrados: " + numLibros);
		System.out.println("Número de autores registrados: " + numAutores);

		// Estadísticas sobre las descargas de los libros
		mostrarEstadisticasDeDescargas();

		// Mostrar el autor con más libros
		Author topAuthor = repository.findAuthorWithMostBooks().stream().findFirst().orElse(null);
		if (topAuthor != null) {
			System.out.println("Autor con más libros: " + topAuthor.getName() + " (" + topAuthor.getBooks().size() + " libros)");
		} else {
			System.out.println("No hay autores registrados.");
		}

		// Mostrar el libro con más descargas
		Book topBook = repository.findBookWithMostDownloads().stream().findFirst().orElse(null);
		if (topBook != null) {
			System.out.println("Libro con más descargas: " + topBook.getTitle() + " (" + topBook.getDownloadCount() + " descargas)");
		} else {
			System.out.println("No hay libros registrados.");
		}
	}

	private void mostrarEstadisticasDeDescargas() {
		List<Book> allBooks = repository.findAllBooks();  // Obtener todos los libros

		// Usamos DoubleSummaryStatistics para obtener estadísticas de las descargas
		DoubleSummaryStatistics stats = allBooks.stream()
				.mapToDouble(book -> book.getDownloadCount() != null ? book.getDownloadCount() : 0)  // Convertimos a double
				.summaryStatistics();  // Calculamos las estadísticas

		System.out.println("Estadísticas de descargas:");
		System.out.println("Total de descargas: " + stats.getSum());
		System.out.println("Promedio de descargas por libro: " + stats.getAverage());
		System.out.println("Máxima cantidad de descargas: " + stats.getMax());
		System.out.println("Mínima cantidad de descargas: " + stats.getMin());
	}
}
