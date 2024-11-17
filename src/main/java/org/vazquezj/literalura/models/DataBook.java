package org.vazquezj.literalura.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataBook(
		@JsonAlias("id") Integer id,
		@JsonAlias("title") String title,
		@JsonAlias("authors") List<DataAuthor> author,
		@JsonAlias("languages") List<String> languages,
		@JsonAlias("download_count") Integer download_count
) {
}
