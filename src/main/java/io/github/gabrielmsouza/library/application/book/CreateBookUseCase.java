package io.github.gabrielmsouza.library.application.book;

import io.github.gabrielmsouza.library.application.UseCase;
import io.github.gabrielmsouza.library.domain.book.Book;
import io.github.gabrielmsouza.library.domain.book.BookGateway;
import io.github.gabrielmsouza.library.domain.exceptions.NotificationException;
import io.github.gabrielmsouza.library.domain.validation.Error;
import io.github.gabrielmsouza.library.domain.validation.ValidationHandler;
import io.github.gabrielmsouza.library.domain.validation.handler.Notification;
import io.github.gabrielmsouza.library.infrastructure.book.models.CreateBookRequest;

import java.util.Objects;


public class CreateBookUseCase implements UseCase<CreateBookUseCase.Input, CreateBookUseCase.Output> {
    private final BookGateway bookGateway;

    public CreateBookUseCase(final BookGateway bookGateway) {
        this.bookGateway = Objects.requireNonNull(bookGateway);
    }

    @Override
    public Output execute(final Input input) {
        final var notification = Notification.create();
        notification.validate(() -> checkIfBookExistsByIsbn(notification, input.isbn()));
        if (notification.hasErrors()) {
            throw NotificationException.with("Invalid book", notification);
        }

        final var book = notification.validate(() -> Book.with(input.title(), input.author(), input.isbn()));
        if (notification.hasErrors()) {
            throw NotificationException.with("Invalid book", notification);
        }
        return Output.from(this.bookGateway.create(book).getId());
    }

    public record Input(String title, String author, String isbn) {
        public static Input from(final CreateBookRequest request) {
            return new Input(request.title(), request.author(), request.isbn());
        }
    }

    public record Output(String id) {
        public static Output from(final String id) {
            return new Output(id);
        }
    }

    private ValidationHandler checkIfBookExistsByIsbn(final Notification notification, final String isbn) {
        if (this.bookGateway.existsByIsbn(isbn)) {
            return notification.append(Error.with("Already exists a book with isbn %s".formatted(isbn)));
        }
        return notification;
    }
}
