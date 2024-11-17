package org.vazquezj.literalura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.vazquezj.literalura.models.Author;
import org.vazquezj.literalura.models.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
	@Query("SELECT a FROM Author a")
	List<Author> findAllAuthors();

	@Query("SELECT a FROM Author a WHERE a.birthYear <= :anio AND (a.deathYear IS NULL OR a.deathYear >= :anio)")
	List<Author> findAuthorsByYear(int anio);
}
