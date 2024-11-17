package org.vazquezj.literalura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.vazquezj.literalura.models.Author;
import org.vazquezj.literalura.models.Book;
import org.vazquezj.literalura.models.Language;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
	@Query("SELECT a FROM Author a")
	List<Author> findAllAuthors();

	@Query("SELECT a FROM Author a WHERE a.birthYear <= :anio AND (a.deathYear IS NULL OR a.deathYear >= :anio)")
	List<Author> findAuthorsByYear(int anio);

	@Query("SELECT b FROM Book b WHERE b.languages = :value")
	List<Book> findBooksByLanguage(Language value);

	@Query("SELECT b FROM Book b")
	List<Book> findAllBooks();

	Optional<Author> findByName(String authorName);
}
