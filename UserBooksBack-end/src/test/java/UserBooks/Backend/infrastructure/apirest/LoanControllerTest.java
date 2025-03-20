package UserBooks.Backend.infrastructure.apirest;

import UserBooks.Backend.domain.model.Book;
import UserBooks.Backend.domain.model.Loan;
import UserBooks.Backend.domain.model.User;
import UserBooks.Backend.service.LoanService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LoanControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanController loanController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Loan loan;
    private Book book;
    private User user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loanController).build();
        book = new Book(1L, "Libro de Prueba", "Autor", "Género", true);
        user = new User(1L, "Usuario Prueba", "user@example.com");
        loan = new Loan(1L, book, user, LocalDate.of(2025, 3, 19), LocalDate.of(2025, 4, 19));
    }

    @Test
    void testCreateLoanSuccess() throws Exception {
        Map<String, String> payload = new HashMap<>();
        payload.put("bookId", "1");
        payload.put("userId", "1");
        payload.put("loanDate", "2025-03-19");
        payload.put("returnDate", "2025-04-19");

        when(loanService.createLoan(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(loan);

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.book.id", is(1)));

        verify(loanService, times(1))
                .createLoan(1L, 1L, LocalDate.parse("2025-03-19"), LocalDate.parse("2025-04-19"));
    }

    @Test
    void testCreateLoanBadRequest() throws Exception {
        Map<String, String> payload = new HashMap<>();
        payload.put("bookId", "abc");

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllLoans() throws Exception {
        Loan loan2 = new Loan(2L, book, user, LocalDate.now(), LocalDate.now().plusDays(10));
        when(loanService.getAllLoans()).thenReturn(Arrays.asList(loan, loan2));

        mockMvc.perform(get("/api/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));

        verify(loanService, times(1)).getAllLoans();
    }

    @Test
    void testGetLoanByIdFound() throws Exception {
        when(loanService.getLoanById(1L)).thenReturn(Optional.of(loan));

        mockMvc.perform(get("/api/loans/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(loanService, times(1)).getLoanById(1L);
    }

    @Test
    void testGetLoanByIdNotFound() throws Exception {
        when(loanService.getLoanById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/loans/1"))
                .andExpect(status().isNotFound());

        verify(loanService, times(1)).getLoanById(1L);
    }

    @Test
    void testUpdateLoanBadRequest() throws Exception {
        Map<String, String> payload = new HashMap<>();
        payload.put("returnDate", "invalid-date");

        mockMvc.perform(put("/api/loans/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testReturnLoan() throws Exception {
        doNothing().when(loanService).returnLoan(1L);

        mockMvc.perform(delete("/api/loans/1"))
                .andExpect(status().isNoContent());

        verify(loanService, times(1)).returnLoan(1L);
    }
}