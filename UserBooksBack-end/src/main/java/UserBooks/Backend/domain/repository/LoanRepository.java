package UserBooks.Backend.domain.repository;

import UserBooks.Backend.domain.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
