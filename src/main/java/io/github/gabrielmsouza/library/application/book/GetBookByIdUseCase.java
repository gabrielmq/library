package io.github.gabrielmsouza.library.application.book;

import io.github.gabrielmsouza.library.application.UseCase;
import io.github.gabrielmsouza.library.domain.book.Book;
import io.github.gabrielmsouza.library.domain.book.BookGateway;
import io.github.gabrielmsouza.library.domain.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

public class GetBookByIdUseCase implements UseCase<String, GetBookByIdUseCase.Output> {
    private final BookGateway bookGateway;

    public GetBookByIdUseCase(final BookGateway bookGateway) {
        this.bookGateway = Objects.requireNonNull(bookGateway);
    }

    @Override
    public Output execute(final String id) {
        return this.bookGateway.findById(id)
                .map(Output::from)
                .orElseThrow(() -> NotFoundException.with(Book.class, id));
    }

    public record Output(String id, String title, String author, String isbn) {
        public static Output from(final Book book) {
            return new Output(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn());
        }
    }
}
