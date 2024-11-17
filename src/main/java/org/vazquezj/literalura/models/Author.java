package org.vazquezj.literalura.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "authors")
public class Author {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(unique = true) //se indica que el nombre es unico y no se puede repetir
	private String name;
	private Integer birthYear;
	private Integer deathYear;
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Book> books = new ArrayList<>();

	public Author() {
	}

	public Author(DataAuthor dauthor) {
		this.name = dauthor.name();
		this.birthYear = dauthor.birth_date();
		this.deathYear = dauthor.death_date();
	}

	public String getName() {
		return name;
	}

	public Integer getBirthYear() {
		return birthYear;
	}

	public Integer getDeathYear() {
		return deathYear;
	}

	public List<Book> getBooks() {
		return books;
	}

	@Override
	public String toString() {
		String booksString = books.stream()
				.map(Book::getTitle)
				.collect(Collectors.joining(", "));
		return "-----------------------------"
				+ "\n" + "Nombre: " + name
				+ "\n" + "Año de nacimiento: " + birthYear
				+ "\n" + "Año de defunción: " + deathYear
				+ "\n" + "Libros: " + booksString
				+ "\n----------------------------";
	}
}
