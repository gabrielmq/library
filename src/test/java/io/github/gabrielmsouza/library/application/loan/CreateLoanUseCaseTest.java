package io.github.gabrielmsouza.library.application.loan;

import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.application.UseCaseTest;
import io.github.gabrielmsouza.library.application.loan.CreateLoanUseCase.Input;
import io.github.gabrielmsouza.library.domain.book.Book;
import io.github.gabrielmsouza.library.domain.book.BookGateway;
import io.github.gabrielmsouza.library.domain.customer.Customer;
import io.github.gabrielmsouza.library.domain.customer.CustomerGateway;
import io.github.gabrielmsouza.library.domain.exceptions.NotFoundException;
import io.github.gabrielmsouza.library.domain.exceptions.NotificationException;
import io.github.gabrielmsouza.library.domain.loan.Loan;
import io.github.gabrielmsouza.library.domain.loan.LoanGateway;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateLoanUseCaseTest extends UseCaseTest {
    @InjectMocks
    private CreateLoanUseCase useCase;

    @Mock
    private LoanGateway loanGateway;

    @Mock
    private BookGateway bookGateway;

    @Mock
    private CustomerGateway customerGateway;

    @Captor
    private ArgumentCaptor<Loan> captor;

    @Test
    void givenAValidParams_whenCallsCreateLoan_thenShouldReturnLoanId() {
        // given
        final var customer = Customer.with(Fixture.Customer.name(), Fixture.Customer.email());
        final var expectedCustomerId = customer.getId();

        final var book = Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn());
        final var expectedIsbn = book.getIsbn();

        when(this.loanGateway.existsByBookIdAndNotReturned(book.getId())).thenReturn(false);
        when(this.bookGateway.findByIsbn(expectedIsbn)).thenReturn(Optional.of(book));
        when(this.customerGateway.findById(expectedCustomerId)).thenReturn(Optional.of(customer));
        when(this.loanGateway.create(any())).thenAnswer(returnsFirstArg());

        final var input = new Input(expectedIsbn, expectedCustomerId);

        // when
        final var actualOutput = this.useCase.execute(input);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(this.bookGateway).findByIsbn(expectedIsbn);

        verify(this.loanGateway).create(this.captor.capture());
        final var actualLoan = this.captor.getValue();
        assertEquals(book.getId(), actualLoan.getBookId());
        assertEquals(expectedCustomerId, actualLoan.getCustomerId());
        assertNotNull(actualLoan.getLoanDate());
        assertFalse(actualLoan.isReturned());
    }

    @Test
    void givenAnInvalidIsbn_whenCallsCreateLoan_thenShouldThrowsNotificationException() {
        // given
        final String expectedIsbn = null;
        final var expectedCustomerId = Fixture.randomId();

        final var expectedErrorMessage = "Book with ISBN " + expectedIsbn + " was not found";

        when(this.bookGateway.findByIsbn(expectedIsbn)).thenReturn(Optional.empty());

        final var input = new Input(expectedIsbn, expectedCustomerId);

        // when
        final var actualException = assertThrows(NotFoundException.class, () -> this.useCase.execute(input));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(this.bookGateway).findByIsbn(expectedIsbn);
    }

    @Test
    void givenAnInvalidCustomerId_whenCallsCreateLoan_thenShouldReturnNotificationException() {
        // given
        final var expectedIsbn = Fixture.Book.isbn();
        final String expectedCustomerId = "123";

        final var expectedErrorMessage = "Customer with ID 123 was not found";

        final var book = Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn());

        when(this.bookGateway.findByIsbn(expectedIsbn)).thenReturn(Optional.of(book));
        when(this.customerGateway.findById(expectedCustomerId)).thenReturn(Optional.empty());

        final var input = new Input(expectedIsbn, expectedCustomerId);

        // when
        final var actualException = assertThrows(NotFoundException.class, () -> this.useCase.execute(input));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(this.bookGateway).findByIsbn(expectedIsbn);
        verify(this.customerGateway).findById(expectedCustomerId);
    }

    @Test
    void givenABookAlreadyLoaned_whenCallsCreateLoan_thenShouldReturnNotificationException() {
        // given
        final var customer = Customer.with(Fixture.Customer.name(), Fixture.Customer.email());
        final var expectedCustomerId = customer.getId();

        final var book = Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn());
        final var expectedBookId = book.getId();
        final var expectedIsbn = book.getIsbn();

        final var expectedErrorMessage = "Book with ID %s is already on loan".formatted(expectedBookId);
        final var expectedErrorSize = 1;

        when(this.loanGateway.existsByBookIdAndNotReturned(expectedBookId)).thenReturn(true);
        when(this.bookGateway.findByIsbn(expectedIsbn)).thenReturn(Optional.of(book));
        when(this.customerGateway.findById(expectedCustomerId)).thenReturn(Optional.of(customer));

        final var input = new Input(expectedIsbn, expectedCustomerId);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(input));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorSize, actualException.getErrors().size());

        verify(bookGateway).findByIsbn(expectedIsbn);
        verify(loanGateway).existsByBookIdAndNotReturned(book.getId());
        verify(customerGateway).findById(expectedCustomerId);
    }
}