package io.github.gabrielmsouza.library.application.loan;

import io.github.gabrielmsouza.library.application.UseCase;
import io.github.gabrielmsouza.library.domain.loan.Loan;
import io.github.gabrielmsouza.library.domain.loan.LoanGateway;
import io.github.gabrielmsouza.library.domain.loan.LoanSearchQuery;
import io.github.gabrielmsouza.library.domain.pagination.Pagination;

import java.time.Instant;
import java.util.Objects;

public class ListLoansUseCase implements UseCase<LoanSearchQuery, Pagination<ListLoansUseCase.Output>> {
    private final LoanGateway loanGateway;

    public ListLoansUseCase(final LoanGateway loanGateway) {
        this.loanGateway = Objects.requireNonNull(loanGateway);
    }

    @Override
    public Pagination<Output> execute(final LoanSearchQuery loanSearchQuery) {
        return this.loanGateway.findAll(loanSearchQuery).map(Output::from);
    }

    public record Output(String id, String customerId, String bookId, Instant loanDate, Instant returnDate, boolean returned) {
        public static Output from(final Loan loan) {
            return new Output(
                loan.getId(),
                loan.getCustomerId(),
                loan.getBookId(),
                loan.getLoanDate(),
                loan.getReturnDate(),
                loan.isReturned()
            );
        }
    }
}
