package io.github.gabrielmsouza.library.application.book;

import io.github.gabrielmsouza.library.application.UseCase;
import io.github.gabrielmsouza.library.domain.book.Book;
import io.github.gabrielmsouza.library.domain.book.BookGateway;
import io.github.gabrielmsouza.library.domain.exceptions.NotFoundException;
import io.github.gabrielmsouza.library.domain.exceptions.NotificationException;
import io.github.gabrielmsouza.library.domain.validation.handler.Notification;
import io.github.gabrielmsouza.library.infrastructure.book.models.UpdateBookRequest;

import java.util.Objects;
import java.util.function.Supplier;

public class UpdateBookUseCase implements UseCase<UpdateBookUseCase.Input, UpdateBookUseCase.Output> {
    private final BookGateway gateway;

    public UpdateBookUseCase(final BookGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public Output execute(final Input input) {
        final var bookId = input.id();
        final var book = gateway.findById(bookId).orElseThrow(notFound(bookId));
        final var notification = Notification.create();
        notification.validate(() -> book.update(input.title(), input.author(), input.isbn()));
        if (notification.hasErrors()) {
            throw new NotificationException("Could not update Book %s".formatted(bookId), notification);
        }
        return Output.from(gateway.update(book).getId());
    }

    public record Input(String id, String title, String author, String isbn) {
        public static Input from(final String id, final UpdateBookRequest request) {
            return new Input(id, request.title(), request.author(), request.isbn());

        }
    }

    public record Output(String id) {
        public static Output from(String id) {
            return new Output(id);
        }
    }

    private Supplier<NotFoundException> notFound(final String id) {
        return () -> NotFoundException.with(Book.class, id);
    }
}
