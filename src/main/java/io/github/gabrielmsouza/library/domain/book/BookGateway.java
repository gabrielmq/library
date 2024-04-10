package io.github.gabrielmsouza.library.domain.book;

import io.github.gabrielmsouza.library.domain.pagination.Pagination;
import io.github.gabrielmsouza.library.domain.pagination.SearchQuery;

import java.util.Optional;

public interface BookGateway {
    Book create(Book book);
    Book update(Book book);
    Optional<Book> findById(String id);
    boolean existsByIsbn(String isbn);
    void deleteById(String id);
    Pagination<Book> findAll(SearchQuery query);
    Optional<Book> findByIsbn(String isbn);
}
