package io.github.gabrielmsouza.library.application.book;

import io.github.gabrielmsouza.library.application.UseCase;
import io.github.gabrielmsouza.library.domain.book.Book;
import io.github.gabrielmsouza.library.domain.book.BookGateway;
import io.github.gabrielmsouza.library.domain.pagination.Pagination;
import io.github.gabrielmsouza.library.domain.pagination.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.Objects;

public class ListBooksUseCase implements UseCase<SearchQuery, Pagination<ListBooksUseCase.Output>> {
    private final BookGateway bookGateway;

    public ListBooksUseCase(final BookGateway bookGateway) {
        this.bookGateway = Objects.requireNonNull(bookGateway);
    }

    public Pagination<Output> execute(final SearchQuery aQuery) {
        return bookGateway.findAll(aQuery).map(Output::from);
    }

    public record Output(
        String id,
        String title,
        String author,
        String isbn
    ) {
        public static Output from(final Book book) {
            return new Output(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn());
        }
    }
}
