package io.github.gabrielmsouza.library.infrastructure.configuration.usecases;

import io.github.gabrielmsouza.library.application.loan.CreateLoanUseCase;
import io.github.gabrielmsouza.library.application.loan.ListLoansUseCase;
import io.github.gabrielmsouza.library.application.loan.NotifyLateLoansUseCase;
import io.github.gabrielmsouza.library.application.loan.ReturnLoanUseCase;
import io.github.gabrielmsouza.library.domain.book.BookGateway;
import io.github.gabrielmsouza.library.domain.customer.CustomerGateway;
import io.github.gabrielmsouza.library.domain.loan.LoanGateway;
import io.github.gabrielmsouza.library.domain.mail.EmailGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration(proxyBeanMethods = false)
public class LoanUseCaseConfiguration {
    private final LoanGateway loanGateway;
    private final BookGateway bookGateway;
    private final CustomerGateway customerGateway;
    private final EmailGateway mailGateway;

    public LoanUseCaseConfiguration(
            final LoanGateway loanGateway,
            final BookGateway bookGateway,
            final CustomerGateway customerGateway,
            final EmailGateway mailGateway
    ) {
        this.loanGateway = Objects.requireNonNull(loanGateway);
        this.bookGateway = Objects.requireNonNull(bookGateway);
        this.customerGateway = Objects.requireNonNull(customerGateway);
        this.mailGateway = Objects.requireNonNull(mailGateway);
    }

    @Bean
    CreateLoanUseCase createLoanUseCase() {
        return new CreateLoanUseCase(this.loanGateway, this.bookGateway, this.customerGateway);
    }

    @Bean
    ReturnLoanUseCase returnLoanUseCase() {
        return new ReturnLoanUseCase(this.loanGateway);
    }

    @Bean
    ListLoansUseCase listLoansUseCase() {
        return new ListLoansUseCase(this.loanGateway);
    }

    @Bean
    NotifyLateLoansUseCase notifyLateLoansUseCase() {
        return new NotifyLateLoansUseCase(this.loanGateway, this.customerGateway, this.mailGateway);
    }
}
