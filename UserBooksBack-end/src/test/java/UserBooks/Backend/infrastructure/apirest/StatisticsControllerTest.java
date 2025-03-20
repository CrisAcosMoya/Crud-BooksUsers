package UserBooks.Backend.infrastructure.apirest;

import UserBooks.Backend.domain.model.Book;
import UserBooks.Backend.domain.repository.BookRepository;
import UserBooks.Backend.domain.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StatisticsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private StatisticsController statisticsController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(statisticsController).build();
    }

    @Test
    void testGetBookStatistics() throws Exception {
        when(bookRepository.count()).thenReturn(3L);
        when(bookRepository.findAll()).thenReturn(Arrays.asList(
                new Book(1L, "Libro 1", "Autor 1", "Género", true),
                new Book(2L, "Libro 2", "Autor 2", "Género", false),
                new Book(3L, "Libro 3", "Autor 3", "Género", true)
        ));

        mockMvc.perform(get("/api/statistics/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalBooks", is(3)))
                .andExpect(jsonPath("$.availableBooks", is(2)));
    }

    @Test
    void testGetLoanStatistics() throws Exception {
        when(loanRepository.count()).thenReturn(5L);

        mockMvc.perform(get("/api/statistics/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalLoans", is(5)));
    }
}