package io.github.gabrielmsouza.library.infrastructure.book;


import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.domain.book.Book;
import io.github.gabrielmsouza.library.domain.book.BookGateway;
import io.github.gabrielmsouza.library.domain.pagination.SearchQuery;
import io.github.gabrielmsouza.library.infrastructure.H2GatewayTest;
import io.github.gabrielmsouza.library.infrastructure.book.persistnce.BookJpaEntity;
import io.github.gabrielmsouza.library.infrastructure.book.persistnce.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@H2GatewayTest
class BookH2GatewayTest {

    @Autowired
    private BookH2Gateway gateway;

    @Autowired
    private BookRepository repository;

    @Test
    void givenAValidBook_whenCallsCreate_thenShouldPersistIt() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var book = Book.with(expectedTitle, expectedAuthor, expectedIsbn);

        final var expectedId = book.getId();

        assertEquals(0, this.repository.count());

        // when
        final var actualBook = this.gateway.create(Book.with(book));

        // then
        assertEquals(1, this.repository.count());
        assertEquals(expectedId, actualBook.getId());
        assertEquals(expectedTitle, actualBook.getTitle());
        assertEquals(expectedAuthor, actualBook.getAuthor());
        assertEquals(expectedIsbn, actualBook.getIsbn());

        final var persistedBook = this.repository.findById(expectedId).get();
        assertEquals(expectedId, persistedBook.getId());
        assertEquals(expectedTitle, persistedBook.getTitle());
        assertEquals(expectedAuthor, persistedBook.getAuthor());
        assertEquals(expectedIsbn, persistedBook.getIsbn());
    }

    @Test
    void givenAPrePersistedBooks_whenCallsExistsByIsbn_thenShouldReturnTrue() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedExistingBook = true;

        final var book = Book.with(expectedTitle, expectedAuthor, expectedIsbn);

        assertEquals(0, this.repository.count());

        this.repository.save(BookJpaEntity.from(book));

        // when
        final var actualResult = this.gateway.existsByIsbn(expectedIsbn);

        // then
        assertEquals(expectedExistingBook, actualResult);
    }

    @Test
    void givenAPrePersistedBooks_whenCallsExistsByIsbn_thenShouldReturnFalse() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedExistingBook = false;

        final var book = Book.with(expectedTitle, expectedAuthor, expectedIsbn);

        assertEquals(0, this.repository.count());

        this.repository.save(BookJpaEntity.from(book));

        // when
        final var actualResult = this.gateway.existsByIsbn(Fixture.Book.isbn());

        // then
        assertEquals(expectedExistingBook, actualResult);
    }

    @Test
    void givenAValidId_whenCallsFindById_thenShouldReturnIt() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var book = Book.with(expectedTitle, expectedAuthor, expectedIsbn);

        final var expectedId = book.getId();

        this.repository.saveAndFlush(BookJpaEntity.from(book));
        assertEquals(1, this.repository.count());

        // when
        final var actualBook = this.gateway.findById(expectedId).get();

        // then
        assertEquals(expectedId, actualBook.getId());
        assertEquals(expectedTitle, actualBook.getTitle());
        assertEquals(expectedAuthor, actualBook.getAuthor());
        assertEquals(expectedIsbn, actualBook.getIsbn());
    }

    @Test
    void givenAnInvalidId_whenCallsFindById_thenShouldReturnEmpty() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var book = Book.with(expectedTitle, expectedAuthor, expectedIsbn);

        this.repository.saveAndFlush(BookJpaEntity.from(book));
        assertEquals(1, this.repository.count());

        // when
        final var actualBook = this.gateway.findById("123");

        // then
        assertTrue(actualBook.isEmpty());
    }

    @Test
    void givenAValidId_whenCallsDeleteById_theShouldDeleteIt() {
        // given
        final var book = Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn());

        this.repository.saveAndFlush(BookJpaEntity.from(book));
        assertEquals(1, this.repository.count());

        // when
        this.gateway.deleteById(book.getId());

        // then
        assertEquals(0, this.repository.count());
    }

    @Test
    void givenAValidId_whenCallsDeleteByIdAndBookDoesNotExists_thenShouldBeIgnored() {
        // given
        final var book =  Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn());

        this.repository.saveAndFlush(BookJpaEntity.from(book));
        assertEquals(1, this.repository.count());

        // when
        this.gateway.deleteById("123");

        // then
        assertEquals(1, this.repository.count());
    }

    @Test
    void givenAValidBook_whenCallsUpdate_thenShouldRefreshIt() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var book = Book.with("xpto", "xpto", "xpto");

        final var expectedId = book.getId();

        final var currentBook = this.repository.saveAndFlush(BookJpaEntity.from(book));
        assertEquals(1, this.repository.count());
        assertEquals("xpto", currentBook.getTitle());
        assertEquals("xpto", currentBook.getAuthor());
        assertEquals("xpto", currentBook.getIsbn());

        // when
        final var actualBook = this.gateway.update(
            Book.with(book)
                    .update(expectedTitle, expectedAuthor, expectedIsbn)
        );

        // then
        assertEquals(1, this.repository.count());
        assertEquals(expectedId, actualBook.getId());
        assertEquals(expectedTitle, actualBook.getTitle());
        assertEquals(expectedAuthor, actualBook.getAuthor());
        assertEquals(expectedIsbn, actualBook.getIsbn());

        final var persistedBook = this.repository.findById(expectedId).get();
        assertEquals(expectedId, persistedBook.getId());
        assertEquals(expectedTitle, persistedBook.getTitle());
        assertEquals(expectedAuthor, persistedBook.getAuthor());
        assertEquals(expectedIsbn, persistedBook.getIsbn());
    }

    @Test
    void givenAPrePersistedBooks_whenCallsFindAll_thenShouldReturnAll() {
        // given
        final var book1 = Book.with("Book 1", "Author 1", "1");
        final var book2 = Book.with("Book 2", "Author 2", "2");

        final var books = List.of(book1, book2);

        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 2;

        assertEquals(0, this.repository.count());

        books.forEach(book -> this.repository.save(BookJpaEntity.from(book)));

        assertEquals(2, this.repository.count());

        final var query = new SearchQuery(expectedPage, expectedPerPage, "", "isbn", "asc");

        // when
        final var actualResult = this.gateway.findAll(query);

        // then
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(book1.getId(), actualResult.items().get(0).getId());
    }

    @Test
    void givenAEmptyBookTables_whenCallsFindAll_thenShouldReturnEmptyPage() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        assertEquals(0, this.repository.count());

        final var query = new SearchQuery(expectedPage, expectedPerPage, "", "isbn", "asc");

        // when
        final var actualResult = this.gateway.findAll(query);

        // then
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(0, actualResult.items().size());
    }

    @Test
    void givenFollowPagination_whenCallsFindAllWithPage1_thenShouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var book1 = Book.with("Book 1", "Author 1", "1");
        final var book2 = Book.with("Book 2", "Author 2", "2");
        final var book3 = Book.with("Book 3", "Author 3", "3");

        assertEquals(0, this.repository.count());

        this.repository.saveAll(List.of(
                BookJpaEntity.from(book1),
                BookJpaEntity.from(book2),
                BookJpaEntity.from(book3)
        ));

        assertEquals(3, this.repository.count());

        var query = new SearchQuery(0, 1, "", "isbn", "asc");
        var actualResult = this.gateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(book1.getId(), actualResult.items().get(0).getId());

        // Page 1
        expectedPage = 1;

        query = new SearchQuery(1, 1, "", "isbn", "asc");
        actualResult = this.gateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(book2.getId(), actualResult.items().get(0).getId());

        // Page 2
        expectedPage = 2;

        query = new SearchQuery(2, 1, "", "isbn", "asc");
        actualResult = this.gateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(book3.getId(), actualResult.items().get(0).getId());
    }

    @Test
    void givenAValidIsbn_whenCallsFindByIsbn_thenShouldReturnIt() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var book = Book.with(expectedTitle, expectedAuthor, expectedIsbn);

        this.repository.saveAndFlush(BookJpaEntity.from(book));
        assertEquals(1, this.repository.count());

        // when
        final var actualBook = this.gateway.findByIsbn(expectedIsbn).get();

        // then
        assertEquals(expectedTitle, actualBook.getTitle());
        assertEquals(expectedAuthor, actualBook.getAuthor());
        assertEquals(expectedIsbn, actualBook.getIsbn());
    }

    @Test
    void givenAValidIsbn_whenCallsFindByIsbn_thenShouldReturnEmpty() {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var book = Book.with(expectedTitle, expectedAuthor, expectedIsbn);

        this.repository.saveAndFlush(BookJpaEntity.from(book));
        assertEquals(1, this.repository.count());

        // when
        final var actualBook = this.gateway.findByIsbn("123");

        // then
        assertTrue(actualBook.isEmpty());
    }
}