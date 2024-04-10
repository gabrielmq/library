package io.github.gabrielmsouza.library.application.loan;

import io.github.gabrielmsouza.library.application.UnaryUseCase;
import io.github.gabrielmsouza.library.domain.exceptions.NotFoundException;
import io.github.gabrielmsouza.library.domain.loan.Loan;
import io.github.gabrielmsouza.library.domain.loan.LoanGateway;

import java.util.Objects;

public class ReturnLoanUseCase implements UnaryUseCase<String> {
    private final LoanGateway loanGateway;

    public ReturnLoanUseCase(final LoanGateway loanGateway) {
        this.loanGateway = Objects.requireNonNull(loanGateway);
    }

    @Override
    public void execute(final String loanId) {
        this.loanGateway
            .findById(loanId)
            .ifPresentOrElse(this::returned, notFound(loanId));
    }

    private void returned(final Loan loan) {
        loan.returned();
        this.loanGateway.update(loan);
    }

    private Runnable notFound(final String loanId) {
        return () -> { throw NotFoundException.with(Loan.class, loanId); };
    }
}
