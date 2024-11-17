package org.vazquezj.literalura.models;

import jakarta.persistence.*;

@Entity
@Table(name = "authors")
public class Author {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private Integer birthYear;
	private Integer deathYear;
	@ManyToOne
	private Book book;

	public Author() {
	}

	public Author(DataAuthor dauthor, Book book) {
		this.name = dauthor.name();
		this.birthYear = dauthor.birth_date();
		this.deathYear = dauthor.death_date();
		this.book = book;
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

	public Book getBook() {
		return book;
	}

	@Override
	public String toString() {
		return "\t" + name;
	}
}
