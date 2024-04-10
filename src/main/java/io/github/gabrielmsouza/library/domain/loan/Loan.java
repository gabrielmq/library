package io.github.gabrielmsouza.library.domain.loan;

import io.github.gabrielmsouza.library.domain.exceptions.NotificationException;
import io.github.gabrielmsouza.library.domain.validation.Error;
import io.github.gabrielmsouza.library.domain.validation.ValidationHandler;
import io.github.gabrielmsouza.library.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Loan {
    private final String id;
    private final String customerId;
    private final String bookId;
    private final Instant loanDate;
    private Instant returnDate;
    private boolean returned;

    private Loan(
            final String id,
            final String customerId,
            final String bookId,
            final Instant loanDate,
            final Instant returnDate,
            final boolean returned
    ) {
        this.id = id;
        this.customerId = customerId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.returned = returned;
        selfValidation();
    }

    public static Loan with(final String customerId, final String bookId) {
        final var id = UUID.randomUUID().toString().toLowerCase().replace("-", "");
        final var loanDate = Instant.now();
        return new Loan(id, customerId, bookId, loanDate, null, false);
    }

    public static Loan with(
        final String id,
        final String customerId,
        final String bookId,
        final Instant loanDate,
        final Instant returnDate,
        final boolean returned
    ) {
        return new Loan(id, customerId, bookId, loanDate, returnDate, returned);
    }

    public static Loan with(final Loan loan) {
        return new Loan(
                loan.getId(),
                loan.getCustomerId(),
                loan.getBookId(),
                loan.getLoanDate(),
                loan.getReturnDate(),
                loan.isReturned()
        );
    }

    public void validate(final ValidationHandler handler) {
        if (Objects.isNull(this.id) || this.id.isBlank()) {
            handler.append(Error.with("'id' should not be empty"));
        }

        if (Objects.isNull(this.customerId) || this.customerId.isBlank()) {
            handler.append(Error.with("'customerId' should not be empty"));
        }

        if (Objects.isNull(this.bookId) || this.bookId.isBlank()) {
            handler.append(Error.with("'bookId' should not be empty"));
        }
    }

    public void returned() {
        this.returned = true;
        this.returnDate = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getBookId() {
        return bookId;
    }

    public Instant getLoanDate() {
        return loanDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public Instant getReturnDate() {
        return returnDate;
    }

    private void selfValidation() {
        final var notification = Notification.create();
        validate(notification);
        if (notification.hasErrors()) {
            throw NotificationException.with("Failed to create a Loan", notification);
        }
    }
}
