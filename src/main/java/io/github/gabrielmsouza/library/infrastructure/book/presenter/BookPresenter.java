package io.github.gabrielmsouza.library.infrastructure.book.presenter;

import io.github.gabrielmsouza.library.application.book.GetBookByIdUseCase;
import io.github.gabrielmsouza.library.application.book.ListBooksUseCase;
import io.github.gabrielmsouza.library.infrastructure.book.models.BookResponse;
import io.github.gabrielmsouza.library.infrastructure.book.models.ListBooksResponse;

public final class BookPresenter {
    private BookPresenter() {
    }

    public static BookResponse present(final GetBookByIdUseCase.Output output) {
        return new BookResponse(
            output.id(),
            output.title(),
            output.author(),
            output.isbn()
        );
    }

    public static ListBooksResponse present(final ListBooksUseCase.Output output) {
        return new ListBooksResponse(
                output.id(),
                output.title(),
                output.author(),
                output.isbn()
        );
    }
}
