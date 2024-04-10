package io.github.gabrielmsouza.library.infrastructure.api.controllers;

import io.github.gabrielmsouza.library.application.book.*;
import io.github.gabrielmsouza.library.domain.pagination.Pagination;
import io.github.gabrielmsouza.library.domain.pagination.SearchQuery;
import io.github.gabrielmsouza.library.infrastructure.api.BookAPI;
import io.github.gabrielmsouza.library.infrastructure.book.models.ListBooksResponse;
import io.github.gabrielmsouza.library.infrastructure.book.models.UpdateBookRequest;
import io.github.gabrielmsouza.library.infrastructure.book.presenter.BookPresenter;
import io.github.gabrielmsouza.library.infrastructure.book.models.BookResponse;
import io.github.gabrielmsouza.library.infrastructure.book.models.CreateBookRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class BookController implements BookAPI {
    private final CreateBookUseCase createBookUseCase;
    private final GetBookByIdUseCase getBookByIdUseCase;
    private final DeleteBookUseCase deleteBookUseCase;
    private final UpdateBookUseCase updateBookUseCase;
    private final ListBooksUseCase listBooksUseCase;
    public BookController(
            final CreateBookUseCase createBookUseCase,
            final GetBookByIdUseCase getBookByIdUseCase,
            final DeleteBookUseCase deleteBookUseCase,
            final UpdateBookUseCase updateBookUseCase,
            final ListBooksUseCase listBooksUseCase
    ) {
        this.createBookUseCase = Objects.requireNonNull(createBookUseCase);
        this.getBookByIdUseCase = Objects.requireNonNull(getBookByIdUseCase);
        this.deleteBookUseCase = Objects.requireNonNull(deleteBookUseCase);
        this.updateBookUseCase = Objects.requireNonNull(updateBookUseCase);
        this.listBooksUseCase = Objects.requireNonNull(listBooksUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateBookRequest request) {
        final var output = this.createBookUseCase.execute(CreateBookUseCase.Input.from(request));
        return ResponseEntity
                .created(URI.create("/books/"+output.id()))
                .body(output);
    }

    @Override
    public ResponseEntity<BookResponse> getById(final String id) {
        return ResponseEntity.ok(BookPresenter.present(this.getBookByIdUseCase.execute(id)));
    }

    @Override
    public void deleteById(final String id) {
        this.deleteBookUseCase.execute(id);
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateBookRequest request) {
        return ResponseEntity.ok(this.updateBookUseCase.execute(UpdateBookUseCase.Input.from(id, request)));
    }

    @Override
    public Pagination<ListBooksResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        final var query = new SearchQuery(page, perPage, search, sort, direction);
        return this.listBooksUseCase.execute(query).map(BookPresenter::present);
    }
}
