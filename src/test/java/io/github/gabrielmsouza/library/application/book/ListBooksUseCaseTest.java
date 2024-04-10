package io.github.gabrielmsouza.library.application.book;

import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.application.UseCaseTest;
import io.github.gabrielmsouza.library.domain.book.Book;
import io.github.gabrielmsouza.library.domain.book.BookGateway;
import io.github.gabrielmsouza.library.domain.pagination.Pagination;
import io.github.gabrielmsouza.library.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ListBooksUseCaseTest extends UseCaseTest {
    @InjectMocks
    private ListBooksUseCase useCase;

    @Mock
    private BookGateway bookGateway;

    @Test
    void givenAValidQuery_whenCallsListBooks_thenShouldReturnAll() {
        // given
        final var books = List.of(
            Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn()),
            Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn())
        );

        final var expectedPage = 1;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "isbn";
        final var expectedDirection = "desc";
        final var expectedTotal = 2;

        final var expectedItems = books.stream().map(ListBooksUseCase.Output::from).toList();
        final var expectedPagination = new Pagination<>(
            expectedPage,
            expectedPerPage,
            expectedTotal,
            books
        );

        final var expectedQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        when(bookGateway.findAll(any())).thenReturn(expectedPagination);

        // when
        final var actualOutput = useCase.execute(expectedQuery);

        // then
        assertEquals(expectedItems, actualOutput.items());
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());

        verify(bookGateway).findAll(expectedQuery);
    }

    @Test
    void givenAValidQuery_whenCallsListBooksAndIsEmpty_thenShouldReturn() {
        // given
        final var expectedPage = 1;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "isbn";
        final var expectedDirection = "desc";
        final var expectedTotal = 0;

        final var expectedItems = List.of();
        final var expectedPagination = new Pagination<>(
            expectedPage,
            expectedPerPage,
            expectedTotal,
            List.<Book>of()
        );

        final var expectedQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        when(bookGateway.findAll(any())).thenReturn(expectedPagination);

        // when
        final var actualOutput = useCase.execute(expectedQuery);

        // then
        assertEquals(expectedItems, actualOutput.items());
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());

        verify(bookGateway).findAll(expectedQuery);
    }

    @Test
    void givenAValidQuery_whenCallsListBooksAndGatewayThrowsRandomException_thenShouldReturnException() {
        // given
        final var expectedPage = 1;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "isbn";
        final var expectedDirection = "desc";

        final var expectedQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        final var expectedErrorMessage = "Gateway error";

        when(bookGateway.findAll(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        // when
        final var actualOutput = assertThrows(RuntimeException.class, () -> useCase.execute(expectedQuery));

        // then
        assertEquals(expectedErrorMessage, actualOutput.getMessage());

        verify(bookGateway).findAll(expectedQuery);
    }
}