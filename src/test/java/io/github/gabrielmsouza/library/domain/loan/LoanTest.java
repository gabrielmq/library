package io.github.gabrielmsouza.library.domain.loan;

import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.application.UseCaseTest;
import io.github.gabrielmsouza.library.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoanTest extends UseCaseTest {

    @Test
    void givenAValidParams_whenCreateLoan_thenShouldInstantiateALoan() {
        // given
        final var customerId = Fixture.randomId();
        final var bookId = Fixture.randomId();

        // when
        final var loan = Loan.with(customerId, bookId);

        // then
        assertNotNull(loan);
        assertNotNull(loan.getId());
        assertEquals(customerId, loan.getCustomerId());
        assertEquals(bookId, loan.getBookId());
        assertFalse(loan.isReturned());
        assertNull(loan.getReturnDate());
    }

    @Test
    void givenAInvalidNullCustomerId_whenCreateLoan_thenShouldReceiveANotification() {
        // given
        final var bookId = Fixture.randomId();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'customerId' should not be empty";

        // when
        final var actualException = assertThrows(NotificationException.class, () -> Loan.with(null, bookId));

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidEmptyCustomerId_whenCreateLoan_thenShouldReceiveANotification() {
        // given
        final var bookId = Fixture.randomId();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'customerId' should not be empty";

        // when
        final var actualException = assertThrows(NotificationException.class, () -> Loan.with("", bookId));

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidNullBookId_whenCreateLoan_thenShouldReceiveANotification() {
        // given
        final var customerId = Fixture.randomId();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'bookId' should not be empty";

        // when
        final var actualException = assertThrows(NotificationException.class, () -> Loan.with(customerId, null));

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidEmptyBookId_whenCreateLoan_thenShouldReceiveANotification() {
        // given
        final var customerId = Fixture.randomId();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'bookId' should not be empty";

        // when
        final var actualException = assertThrows(NotificationException.class, () -> Loan.with(customerId, ""));

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAValidLoan_whenReturnLoan_thenShouldMarkAsReturned() {
        // given
        final var loan = Loan.with(Fixture.randomId(), Fixture.randomId());

        // when
        loan.returned();

        // then
        assertTrue(loan.isReturned());
        assertNotNull(loan.getReturnDate());
    }
}