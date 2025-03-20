package UserBooks.Backend.service;

import UserBooks.Backend.domain.model.Book;
import UserBooks.Backend.domain.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book(1L, "Titulo Original", "Autor Original", "Genero", false);
    }

    @Test
    void testCreateBook() {
        Book toSave = new Book(null, "Nuevo Libro", "Autor Nuevo", "Ficcion", false);
        Book saved = new Book(1L, "Nuevo Libro", "Autor Nuevo", "Ficcion", true);

        when(bookRepository.save(any(Book.class))).thenReturn(saved);

        Book result = bookService.createBook(toSave);
        assertTrue(result.isAvailable());
        assertEquals("Nuevo Libro", result.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = Arrays.asList(book, new Book(2L, "Libro 2", "Autor 2", "Drama", true));
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();
        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetBookByIdFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Optional<Book> result = bookService.getBookById(1L);
        assertTrue(result.isPresent());
        assertEquals("Titulo Original", result.get().getTitle());
    }

    @Test
    void testUpdateBookSuccess() {
        Book updatedBook = new Book(null, "Titulo Actualizado", "Autor Actualizado", "Novela", false);
        Book updatedSaved = new Book(1L, "Titulo Actualizado", "Autor Actualizado", "Novela", false);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedSaved);

        Book result = bookService.updateBook(1L, updatedBook);
        assertEquals("Titulo Actualizado", result.getTitle());
        assertEquals("Autor Actualizado", result.getAuthor());
        assertEquals("Novela", result.getGenre());

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(captor.capture());
        Book savedBook = captor.getValue();
        assertEquals("Titulo Actualizado", savedBook.getTitle());
    }

    @Test
    void testUpdateBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        Book updatedBook = new Book(null, "Titulo Actualizado", "Autor Actualizado", "Novela", false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookService.updateBook(1L, updatedBook));
        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    void testDeleteBook() {
        doNothing().when(bookRepository).deleteById(1L);
        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }
}