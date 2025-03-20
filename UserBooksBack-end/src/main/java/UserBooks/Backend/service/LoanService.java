package UserBooks.Backend.service;

import UserBooks.Backend.domain.model.Book;
import UserBooks.Backend.domain.model.Loan;
import UserBooks.Backend.domain.model.User;
import UserBooks.Backend.domain.repository.BookRepository;
import UserBooks.Backend.domain.repository.LoanRepository;
import UserBooks.Backend.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public Loan createLoan(Long bookId, Long userId, LocalDate loanDate, LocalDate returnDate) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        if (!book.isAvailable()) {
            throw new RuntimeException("Book not available for loan");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setUser(user);
        loan.setLoanDate(loanDate);
        loan.setReturnDate(returnDate);

        book.setAvailable(false);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Optional<Loan> getLoanById(Long id) {
        return loanRepository.findById(id);
    }

    public Loan updateLoan(Long id, LocalDate newReturnDate) {
        return loanRepository.findById(id).map(loan -> {
            loan.setReturnDate(newReturnDate);
            return loanRepository.save(loan);
        }).orElseThrow(() -> new RuntimeException("Loan not found"));
    }

    public void returnLoan(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        Book book = loan.getBook();
        book.setAvailable(true);
        bookRepository.save(book);
        loanRepository.deleteById(id);
    }
}
