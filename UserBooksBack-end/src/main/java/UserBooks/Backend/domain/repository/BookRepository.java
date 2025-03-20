package UserBooks.Backend.domain.repository;

import UserBooks.Backend.domain.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
