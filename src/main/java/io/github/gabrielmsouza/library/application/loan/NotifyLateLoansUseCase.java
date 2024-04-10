package io.github.gabrielmsouza.library.application.loan;

import io.github.gabrielmsouza.library.domain.customer.CustomerGateway;
import io.github.gabrielmsouza.library.domain.loan.LoanGateway;
import io.github.gabrielmsouza.library.domain.mail.EmailGateway;

import java.time.LocalDate;
import java.util.Objects;

public class NotifyLateLoansUseCase {
    private final LoanGateway loanGateway;
    private final CustomerGateway customerGateway;
    private final EmailGateway mailGateway;

    public NotifyLateLoansUseCase(
            final LoanGateway loanGateway,
            final CustomerGateway customerGateway,
            final EmailGateway mailGateway
    ) {
        this.loanGateway = Objects.requireNonNull(loanGateway);
        this.customerGateway = Objects.requireNonNull(customerGateway);
        this.mailGateway = Objects.requireNonNull(mailGateway);
    }

    public void execute() {
        final var loanDays = 4;
        final var date = LocalDate.now().minusDays(loanDays);
        final var loans = this.loanGateway.findAllLateLoans(date);
        loans.forEach(loan ->
            customerGateway.findById(loan.getCustomerId())
                .ifPresent(customer -> this.mailGateway.send(customer.getEmail()))
        );
    }
}
