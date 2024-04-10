package io.github.gabrielmsouza.library.application.loan;

import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.application.UseCaseTest;
import io.github.gabrielmsouza.library.domain.book.Book;
import io.github.gabrielmsouza.library.domain.loan.Loan;
import io.github.gabrielmsouza.library.domain.loan.LoanGateway;
import io.github.gabrielmsouza.library.domain.loan.LoanSearchQuery;
import io.github.gabrielmsouza.library.domain.pagination.Pagination;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ListLoansUseCaseTest extends UseCaseTest {
    @InjectMocks
    private ListLoansUseCase useCase;

    @Mock
    private LoanGateway loanGateway;

    @Test
    void givenAValidQuery_whenCallsListLoans_thenShouldReturnAll() {
        // given
        final var books = List.of(
            Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn()),
            Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn())
        );

        final var loans = books.stream().map(book -> Loan.with(Fixture.randomId(), book.getId())).toList();

        final var expectedPage = 1;
        final var expectedPerPage = 10;
        final var expectedIsbn = books.get(0).getIsbn();
        final var expectedCustomerId = loans.get(0).getCustomerId();
        final var expectedSort = "loan_date";
        final var expectedDirection = "desc";
        final var expectedTotal = 2;

        final var expectedItems = loans.stream().map(ListLoansUseCase.Output::from).toList();
        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                loans
        );

        final var expectedQuery = new LoanSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedIsbn,
                expectedCustomerId,
                expectedSort,
                expectedDirection
        );

        when(this.loanGateway.findAll(any())).thenReturn(expectedPagination);

        // when
        final var actualOutput = useCase.execute(expectedQuery);

        // then
        assertEquals(expectedItems, actualOutput.items());
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());

        verify(this.loanGateway).findAll(expectedQuery);
    }

    @Test
    void givenAValidQuery_whenCallsListBooksAndIsEmpty_thenShouldReturn() {
        // given
        final var expectedPage = 1;
        final var expectedPerPage = 10;
        final var expectedIsbn = "";
        final var expectedCustomerId = "";
        final var expectedSort = "loan_date";
        final var expectedDirection = "desc";
        final var expectedTotal = 0;

        final var expectedItems = List.of();
        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                List.<Loan>of()
        );

        final var expectedQuery = new LoanSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedIsbn,
                expectedCustomerId,
                expectedSort,
                expectedDirection
        );

        when(this.loanGateway.findAll(any())).thenReturn(expectedPagination);

        // when
        final var actualOutput = useCase.execute(expectedQuery);

        // then
        assertEquals(expectedItems, actualOutput.items());
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());

        verify(this.loanGateway).findAll(expectedQuery);
    }

    @Test
    void givenAValidQuery_whenCallsListBooksAndGatewayThrowsRandomException_thenShouldReturnException() {
        // given
        final var expectedPage = 1;
        final var expectedPerPage = 10;
        final var expectedIsbn = "";
        final var expectedCustomerId = "";
        final var expectedSort = "loan_date";
        final var expectedDirection = "desc";

        final var expectedQuery = new LoanSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedIsbn,
                expectedCustomerId,
                expectedSort,
                expectedDirection
        );

        final var expectedErrorMessage = "Gateway error";

        when(this.loanGateway.findAll(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        // when
        final var actualOutput = assertThrows(RuntimeException.class, () -> useCase.execute(expectedQuery));

        // then
        assertEquals(expectedErrorMessage, actualOutput.getMessage());

        verify(this.loanGateway).findAll(expectedQuery);
    }
}