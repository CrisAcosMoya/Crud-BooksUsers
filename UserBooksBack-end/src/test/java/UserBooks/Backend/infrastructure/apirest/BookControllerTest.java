package UserBooks.Backend.infrastructure.apirest;

import UserBooks.Backend.domain.model.Book;
import UserBooks.Backend.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void testCreateBook() throws Exception {
        Book input = new Book(null, "El señor de los anillos", "J.R.R. Tolkien", "Fantasía", false);
        Book created = new Book(1L, "El señor de los anillos", "J.R.R. Tolkien", "Fantasía", true);

        when(bookService.createBook(any(Book.class))).thenReturn(created);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.available", is(true)));

        verify(bookService, times(1)).createBook(any(Book.class));
    }

    @Test
    void testGetAllBooks() throws Exception {
        Book book1 = new Book(1L, "Libro 1", "Autor 1", "Género 1", true);
        Book book2 = new Book(2L, "Libro 2", "Autor 2", "Género 2", false);
        when(bookService.getAllBooks()).thenReturn(Arrays.asList(book1, book2));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testGetBookByIdFound() throws Exception {
        Book book = new Book(1L, "Libro 1", "Autor 1", "Género 1", true);
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Libro 1")));

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void testGetBookByIdNotFound() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void testUpdateBookSuccess() throws Exception {
        Book input = new Book(null, "Libro Actualizado", "Autor Actualizado", "Novela", true);
        Book updated = new Book(1L, "Libro Actualizado", "Autor Actualizado", "Novela", true);

        when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(updated);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Libro Actualizado")));

        verify(bookService, times(1)).updateBook(eq(1L), any(Book.class));
    }

    @Test
    void testUpdateBookNotFound() throws Exception {
        Book input = new Book(null, "Libro Actualizado", "Autor Actualizado", "Novela", true);
        when(bookService.updateBook(eq(1L), any(Book.class)))
                .thenThrow(new RuntimeException("Book not found"));

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).updateBook(eq(1L), any(Book.class));
    }

    @Test
    void testDeleteBook() throws Exception {
        doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBook(1L);
    }
}