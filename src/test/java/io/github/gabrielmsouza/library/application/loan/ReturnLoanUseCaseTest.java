package io.github.gabrielmsouza.library.application.loan;

import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.application.UseCaseTest;
import io.github.gabrielmsouza.library.domain.exceptions.NotFoundException;
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
import static org.mockito.Mockito.*;

class ReturnLoanUseCaseTest extends UseCaseTest {
    @InjectMocks
    private ReturnLoanUseCase useCase;

    @Mock
    private LoanGateway gateway;

    @Captor
    private ArgumentCaptor<Loan> captor;

    @Test
    void givenAValidId_whenCallsReturnLoan_thenShouldBeOk() {
        // given
        final var expectedIsbn = Fixture.Book.isbn();
        final var expectedCustomerId = Fixture.randomId();

        final var loan = Loan.with(expectedCustomerId, expectedIsbn);
        final var expectedId = loan.getId();

        when(this.gateway.findById(expectedId)).thenReturn(Optional.of(loan));
        when(this.gateway.update(any())).thenAnswer(returnsFirstArg());

        // when
        assertDoesNotThrow(() -> this.useCase.execute(expectedId));

        // then
        verify(this.gateway).findById(expectedId);

        verify(this.gateway).update(captor.capture());
        final var actualLoan = captor.getValue();
        assertEquals(expectedId, actualLoan.getId());
        assertEquals(expectedCustomerId, actualLoan.getCustomerId());
        assertEquals(expectedIsbn, actualLoan.getBookId());
        assertTrue(actualLoan.isReturned());
    }

    @Test
    void givenAValidId_whenCallsReturnLoanAndLoanDoesNotExists_thenShouldReceiveException() {
        // given
        final var expectedId = Fixture.randomId();
        final var expectedErrorMessage = "Loan with ID %s was not found".formatted(expectedId);

        when(this.gateway.findById(expectedId)).thenReturn(Optional.empty());

        // when
        final var actualException = assertThrows(NotFoundException.class, () -> this.useCase.execute(expectedId));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(this.gateway).findById(expectedId);
        verify(this.gateway, never()).update(any());
    }
}