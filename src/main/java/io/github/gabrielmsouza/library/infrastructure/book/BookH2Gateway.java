package io.github.gabrielmsouza.library.infrastructure.book;

import io.github.gabrielmsouza.library.domain.book.Book;
import io.github.gabrielmsouza.library.domain.book.BookGateway;
import io.github.gabrielmsouza.library.domain.pagination.Pagination;
import io.github.gabrielmsouza.library.domain.pagination.SearchQuery;
import io.github.gabrielmsouza.library.infrastructure.book.persistnce.BookJpaEntity;
import io.github.gabrielmsouza.library.infrastructure.book.persistnce.BookRepository;
import io.github.gabrielmsouza.library.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Component
public class BookH2Gateway implements BookGateway {
    private final BookRepository bookRepository;

    public BookH2Gateway(final BookRepository bookRepository) {
        this.bookRepository = Objects.requireNonNull(bookRepository);
    }

    @Override
    public Book create(final Book book) {
        return save(book);
    }

    @Override
    public Book update(final Book book) {
        return save(book);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(final String id) {
        return this.bookRepository.findById(id).map(BookJpaEntity::toBook);
    }

    @Override
    public boolean existsByIsbn(final String isbn) {
        return this.bookRepository.existsByIsbn(isbn);
    }

    @Override
    public void deleteById(final String id) {
        if (this.bookRepository.existsById(id)) {
            this.bookRepository.deleteById(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<Book> findAll(final SearchQuery query) {
        final var page = PageRequest.of(
            query.page(),
            query.perPage(),
            Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var where = Optional.ofNullable(query.terms())
            .filter(terms -> !terms.isBlank())
            .map(terms ->
                    SpecificationUtils.<BookJpaEntity>like("title", terms)
                        .or(SpecificationUtils.like("author", terms))
                        .or(SpecificationUtils.like("isbn", terms)))
                .orElse(null);

        final var result = this.bookRepository.findAll(where, page);
        return new Pagination<>(
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getContent().stream().map(BookJpaEntity::toBook).toList()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findByIsbn(final String isbn) {
        return this.bookRepository.findByIsbn(isbn).map(BookJpaEntity::toBook);
    }

    private Book save(final Book book) {
        return this.bookRepository.save(BookJpaEntity.from(book)).toBook();
    }
}
