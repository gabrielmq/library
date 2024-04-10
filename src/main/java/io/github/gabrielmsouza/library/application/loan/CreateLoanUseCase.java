package io.github.gabrielmsouza.library.application.loan;

import io.github.gabrielmsouza.library.application.UseCase;
import io.github.gabrielmsouza.library.domain.book.BookGateway;
import io.github.gabrielmsouza.library.domain.customer.Customer;
import io.github.gabrielmsouza.library.domain.customer.CustomerGateway;
import io.github.gabrielmsouza.library.domain.exceptions.NotFoundException;
import io.github.gabrielmsouza.library.domain.exceptions.NotificationException;
import io.github.gabrielmsouza.library.domain.loan.Loan;
import io.github.gabrielmsouza.library.domain.loan.LoanGateway;
import io.github.gabrielmsouza.library.domain.validation.Error;
import io.github.gabrielmsouza.library.domain.validation.ValidationHandler;
import io.github.gabrielmsouza.library.domain.validation.handler.Notification;
import io.github.gabrielmsouza.library.infrastructure.loan.models.CreateLoanRequest;

import java.util.Objects;
import java.util.function.Supplier;

public class CreateLoanUseCase implements UseCase<CreateLoanUseCase.Input, CreateLoanUseCase.Output> {
    private final LoanGateway loanGateway;
    private final BookGateway bookGateway;
    private final CustomerGateway customerGateway;

    public CreateLoanUseCase(
            final LoanGateway loanGateway,
            final BookGateway bookGateway,
            final CustomerGateway customerGateway
    ) {
        this.loanGateway = Objects.requireNonNull(loanGateway);
        this.bookGateway = Objects.requireNonNull(bookGateway);
        this.customerGateway = Objects.requireNonNull(customerGateway);
    }

    @Override
    public Output execute(final Input input) {
        final var isbn = input.isbn();
        final var book = this.bookGateway.findByIsbn(isbn).orElseThrow(bookNotFound(isbn));
        final var customer = this.customerGateway.findById(input.customerId())
                .orElseThrow(() -> NotFoundException.with(Customer.class, input.customerId()));

        final var notification = Notification.create();
        notification.validate(() -> checkIfBookAlreadyLoaned(notification, book.getId()));
        if (notification.hasErrors()) {
            throw NotificationException.with("Invalid loan", notification);
        }

        final var loan = notification.validate(() -> Loan.with(customer.getId(), book.getId()));
        if (notification.hasErrors()) {
            throw NotificationException.with("Invalid loan", notification);
        }
        return Output.from(this.loanGateway.create(loan).getId());
    }

    public record Input(String isbn, String customerId) {
        public  static Input from(final CreateLoanRequest request) {
            return new Input(request.isbn(), request.customerId());
        }
    }

    public record Output(String id) {
        public static Output from(final String id) {
            return new Output(id);
        }
    }

    private ValidationHandler checkIfBookAlreadyLoaned(final Notification notification, final String bookId) {
        if (this.loanGateway.existsByBookIdAndNotReturned(bookId)) {
            return notification.append(Error.with("Book with ID %s is already on loan".formatted(bookId)));
        }
        return notification;
    }

    private Supplier<NotFoundException> bookNotFound(final String isbn) {
        return () -> NotFoundException.with("Book with ISBN %s was not found".formatted(isbn));
    }
}
