package io.github.gabrielmsouza.library.domain.loan;

import io.github.gabrielmsouza.library.domain.pagination.Pagination;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LoanGateway {
    Loan create(Loan loan);
    Loan update(Loan loan);
    Optional<Loan> findById(String id);
    boolean existsByBookIdAndNotReturned(String bookId);
    Pagination<Loan> findAll(LoanSearchQuery query);
    List<Loan> findAllLateLoans(final LocalDate date);
}
