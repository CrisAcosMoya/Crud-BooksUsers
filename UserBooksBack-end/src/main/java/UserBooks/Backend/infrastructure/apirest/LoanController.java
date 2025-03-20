package UserBooks.Backend.infrastructure.apirest;

import UserBooks.Backend.domain.model.Loan;
import UserBooks.Backend.service.LoanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(
        name = "REST Para la gestion de los prestamos",
        description = "CRUD REST APIs para la creacion, recuperacion, actualizacion y eliminación de prestamos"
)
@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestBody Map<String, String> payload) {
        try {
            Long bookId = Long.parseLong(payload.get("bookId"));
            Long userId = Long.parseLong(payload.get("userId"));
            LocalDate loanDate = LocalDate.parse(payload.get("loanDate"));
            LocalDate returnDate = LocalDate.parse(payload.get("returnDate"));

            Loan loan = loanService.createLoan(bookId, userId, loanDate, returnDate);
            return new ResponseEntity<>(loan, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        return loanService.getLoanById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Loan> updateLoan(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        try {
            LocalDate newReturnDate = LocalDate.parse(payload.get("returnDate"));
            Loan updatedLoan = loanService.updateLoan(id, newReturnDate);
            return ResponseEntity.ok(updatedLoan);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> returnLoan(@PathVariable Long id) {
        loanService.returnLoan(id);
        return ResponseEntity.noContent().build();
    }
}
