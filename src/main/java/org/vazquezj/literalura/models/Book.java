package org.vazquezj.literalura.models;

import java.util.List;

public class Book {
	private Integer id;
	private String title;
	private List<Author> authors;
	private List<Language> languages;
	private Integer downloadCount;

	public Book(DataBook dbook) {
		this.id = dbook.id();
		this.title = dbook.title();
		this.authors = dbook.authors().stream().map(dauthor -> new Author(dauthor)).toList();
		this.languages = dbook.languages().stream().map(code -> Language.fromCode(code)).toList();
		this.downloadCount = dbook.download_count();
	}

	@Override
	public String toString() {
		return "Book{" +
				"id=" + id +
				", title='" + title + '\'' +
				", authors=" + authors +
				", languages=" + languages +
				", downloadCount=" + downloadCount +
				'}';
	}
}
