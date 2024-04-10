package io.github.gabrielmsouza.library.application.book;

import io.github.gabrielmsouza.library.application.UnaryUseCase;
import io.github.gabrielmsouza.library.domain.book.BookGateway;
import org.springframework.stereotype.Component;

import java.util.Objects;

public class DeleteBookUseCase implements UnaryUseCase<String> {
    private final BookGateway gateway;

    public DeleteBookUseCase(final BookGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public void execute(final String id) {
        this.gateway.deleteById(id);
    }
}
