package io.github.gabrielmsouza.library.infrastructure.loan.present;

import io.github.gabrielmsouza.library.application.loan.ListLoansUseCase;
import io.github.gabrielmsouza.library.infrastructure.loan.models.ListLoansResponse;

public final class LoanPresent {
    private LoanPresent() {
    }

    public static ListLoansResponse present(final ListLoansUseCase.Output output) {
        return new ListLoansResponse(
                output.id(),
                output.bookId(),
                output.customerId(),
                output.loanDate(),
                output.returnDate(),
                output.returned()
        );
    }
}
