package org.vazquezj.literalura.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "books")
public class Book {
	@Id
	private Integer id;
	private String title;
	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Author> authors;
	@Enumerated(EnumType.STRING)
	private Language languages;
	private Integer downloadCount;

	public Book() { //aunque ya esta el constructor vacio, es necesario para que funcione el repository
	}

	public Book(DataBook dbook) {
		this.id = dbook.id();
		this.title = dbook.title();
		this.authors = dbook.authors().stream().map(dauthor -> new Author(dauthor, this)).toList();
		this.languages = dbook.languages().stream().findFirst().map(code -> Language.fromCode(code)).orElse(null);
		this.downloadCount = dbook.download_count();
	}

	@Override
	public String toString() {
		return "--------------------" + "\n" +
				"Título: '" + title + '\'' + "\n" +
				"Autores: " + authors.stream().map(Author::toString).collect(Collectors.joining(" | ")) + "\n" +
				"Idima: " + languages + "\n" +
				"Cantidad de descargas: " + downloadCount + "\n" +
				"--------------------";
	}
}
