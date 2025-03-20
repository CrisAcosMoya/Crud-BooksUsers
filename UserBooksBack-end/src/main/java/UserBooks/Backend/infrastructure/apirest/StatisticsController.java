package UserBooks.Backend.infrastructure.apirest;

import UserBooks.Backend.domain.model.Book;
import UserBooks.Backend.domain.repository.BookRepository;
import UserBooks.Backend.domain.repository.LoanRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@Tag(
        name = "REST Para la estadistica y analitica",
        description = "REST APIs para listado estadistico de la gestion de libros y prestamos a usuarios"
)
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    public StatisticsController(BookRepository bookRepository, LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
    }

    @GetMapping("/books")
    public Map<String, Object> getBookStatistics() {
        long totalBooks = bookRepository.count();
        long availableBooks = bookRepository.findAll().stream().filter(Book::isAvailable).count();
        return Map.of(
                "totalBooks", totalBooks,
                "availableBooks", availableBooks
        );
    }

    @GetMapping("/loans")
    public Map<String, Object> getLoanStatistics() {
        long totalLoans = loanRepository.count();
        return Map.of(
                "totalLoans", totalLoans
        );
    }
}
