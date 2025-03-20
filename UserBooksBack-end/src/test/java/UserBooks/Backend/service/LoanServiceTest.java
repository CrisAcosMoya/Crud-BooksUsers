package UserBooks.Backend.service;

import UserBooks.Backend.domain.model.Book;
import UserBooks.Backend.domain.model.Loan;
import UserBooks.Backend.domain.model.User;
import UserBooks.Backend.domain.repository.BookRepository;
import UserBooks.Backend.domain.repository.LoanRepository;
import UserBooks.Backend.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoanService loanService;

    private Book book;
    private User user;

    @BeforeEach
    void setUp() {
        book = new Book(1L, "Libro de Prueba", "Autor Prueba", "Genero", true);
        user = new User(1L, "Usuario Prueba", "user@example.com");
    }

    @Test
    void testCreateLoanSuccess() {
        LocalDate loanDate = LocalDate.of(2025, 3, 19);
        LocalDate returnDate = LocalDate.of(2025, 4, 19);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.save(any(Book.class))).thenReturn(new Book(1L, book.getTitle(), book.getAuthor(), book.getGenre(), false));
        Loan savedLoan = new Loan(1L, book, user, loanDate, returnDate);
        when(loanRepository.save(any(Loan.class))).thenReturn(savedLoan);

        Loan result = loanService.createLoan(1L, 1L, loanDate, returnDate);

        assertNotNull(result);
        assertEquals(1L, result.getBook().getId());
        assertFalse(result.getBook().isAvailable()); // El libro se marca como no disponible

        verify(bookRepository, times(1)).save(any(Book.class));
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void testCreateLoanBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> loanService.createLoan(1L, 1L, LocalDate.now(), LocalDate.now().plusDays(10)));
        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    void testCreateLoanBookNotAvailable() {
        book.setAvailable(false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> loanService.createLoan(1L, 1L, LocalDate.now(), LocalDate.now().plusDays(10)));
        assertEquals("Book not available for loan", exception.getMessage());
    }

    @Test
    void testCreateLoanUserNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> loanService.createLoan(1L, 1L, LocalDate.now(), LocalDate.now().plusDays(10)));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testGetAllLoans() {
        Loan loan1 = new Loan(1L, book, user, LocalDate.now(), LocalDate.now().plusDays(10));
        Loan loan2 = new Loan(2L, book, user, LocalDate.now(), LocalDate.now().plusDays(15));
        List<Loan> loans = Arrays.asList(loan1, loan2);
        when(loanRepository.findAll()).thenReturn(loans);

        List<Loan> result = loanService.getAllLoans();
        assertEquals(2, result.size());
        verify(loanRepository, times(1)).findAll();
    }

    @Test
    void testGetLoanByIdFound() {
        Loan loan = new Loan(1L, book, user, LocalDate.now(), LocalDate.now().plusDays(10));
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        Optional<Loan> result = loanService.getLoanById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testUpdateLoanSuccess() {
        LocalDate newReturnDate = LocalDate.of(2025, 5, 1);
        Loan loan = new Loan(1L, book, user, LocalDate.now(), LocalDate.now().plusDays(10));
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenReturn(new Loan(1L, book, user, loan.getLoanDate(), newReturnDate));

        Loan result = loanService.updateLoan(1L, newReturnDate);
        assertEquals(newReturnDate, result.getReturnDate());
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void testUpdateLoanNotFound() {
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> loanService.updateLoan(1L, LocalDate.now().plusDays(10)));
        assertEquals("Loan not found", exception.getMessage());
    }

    @Test
    void testReturnLoanSuccess() {
        Loan loan = new Loan(1L, book, user, LocalDate.now(), LocalDate.now().plusDays(10));
        book.setAvailable(false);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        doNothing().when(loanRepository).deleteById(1L);
        when(bookRepository.save(any(Book.class))).thenReturn(new Book(1L, book.getTitle(), book.getAuthor(), book.getGenre(), true));

        loanService.returnLoan(1L);
        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(captor.capture());
        assertTrue(captor.getValue().isAvailable());
        verify(loanRepository, times(1)).deleteById(1L);
    }

    @Test
    void testReturnLoanNotFound() {
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> loanService.returnLoan(1L));
        assertEquals("Loan not found", exception.getMessage());
    }
}