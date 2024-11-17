package org.vazquezj.literalura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vazquezj.literalura.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
