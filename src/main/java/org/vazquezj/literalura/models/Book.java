package org.vazquezj.literalura.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "books")
public class Book {
	@Id
	@Column(unique = true)
	private Integer id;
	private String title;
	@ManyToOne
	private Author author;
	@Enumerated(EnumType.STRING)
	private Language languages;
	private Integer downloadCount;

	public Book() { //aunque ya esta el constructor vacio, es necesario para que funcione el repository
	}

	public Book(DataBook dbook) {
		this.id = dbook.id();
		this.title = dbook.title();
		this.languages = dbook.languages().stream().findFirst().map(code -> Language.fromCode(code)).orElse(null);
		this.downloadCount = dbook.download_count();
	}

	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Author getAuthor() {
		return author;
	}

	public Language getLanguages() {
		return languages;
	}

	public Integer getDownloadCount() {
		return downloadCount;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	@Override
	public String toString() {
		return "--------------------" + "\n" +
				"Título: '" + title + '\'' + "\n" +
				"Autores: " + author.getName() + "\n" +
				"Idima: " + languages + "\n" +
				"Cantidad de descargas: " + downloadCount + "\n" +
				"--------------------";
	}
}
