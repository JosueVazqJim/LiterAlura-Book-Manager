package org.vazquezj.literalura.models;

public class Author {
	private String name;
	private Integer birthYear;
	private Integer deathYear;

	public Author(DataAuthor dauthor) {
		this.name = dauthor.name();
		this.birthYear = dauthor.birth_date();
		this.deathYear = dauthor.death_date();
	}

	@Override
	public String toString() {
		return "Author{" +
				"name='" + name + '\'' +
				", birthYear=" + birthYear +
				", deathYear=" + deathYear +
				'}';
	}
}
