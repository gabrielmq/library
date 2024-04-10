package io.github.gabrielmsouza.library.application.customer;

import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.application.UseCaseTest;
import io.github.gabrielmsouza.library.domain.customer.Customer;
import io.github.gabrielmsouza.library.domain.customer.CustomerGateway;
import io.github.gabrielmsouza.library.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateCustomerUseCaseTest extends UseCaseTest {
    @InjectMocks
    private CreateCustomerUseCase useCase;

    @Mock
    private CustomerGateway gateway;

    @Captor
    private ArgumentCaptor<Customer> captor;

    @Test
    void givenAValidParams_whenCallsCreateCustomer_thenShouldReturnCustomerId() {
        // given
        final var expectedName = Fixture.Customer.name();
        final var expectedEmail = Fixture.Customer.email();

        final var actualInput = new CreateCustomerUseCase.Input(expectedName, expectedEmail);

        when(this.gateway.create(any())).thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = this.useCase.execute(actualInput);

        // then
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(this.gateway).create(this.captor.capture());
        final var actualCustomer = this.captor.getValue();
        assertEquals(expectedName, actualCustomer.getName());
        assertEquals(expectedEmail, actualCustomer.getEmail());
    }

    @Test
    void givenAInvalidNullName_whenCallsCreateCustomer_thenShouldReturnNotificationException() {
        // given
        final String expectedName = null;
        final var expectedEmail = Fixture.Customer.email();

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var actualInput = new CreateCustomerUseCase.Input(expectedName, expectedEmail);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(actualInput));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidEmptyName_whenCallsCreateCustomer_thenShouldReturnNotificationException() {
        // given
        final var expectedName = "";
        final var expectedEmail = Fixture.Customer.email();

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var actualInput = new CreateCustomerUseCase.Input(expectedName, expectedEmail);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(actualInput));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidNullEmail_whenCallsCreateCustomer_thenShouldReturnNotificationException() {
        // given
        final var expectedName = Fixture.Customer.name();
        final String expectedEmail = null;

        final var expectedErrorMessage = "'email' should have a valid format";
        final var expectedErrorCount = 1;

        final var actualInput = new CreateCustomerUseCase.Input(expectedName, expectedEmail);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(actualInput));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidEmptyEmail_whenCallsCreateCustomer_thenShouldReturnNotificationException() {
        // given
        final var expectedName = Fixture.Customer.name();
        final var expectedEmail = "";

        final var expectedErrorMessage = "'email' should have a valid format";
        final var expectedErrorCount = 1;

        final var actualInput = new CreateCustomerUseCase.Input(expectedName, expectedEmail);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(actualInput));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidEmailFormat_whenCallsCreateCustomer_thenShouldReturnNotificationException() {
        // given
        final var expectedName = Fixture.Customer.name();
        final var expectedEmail = "invalid-email";

        final var expectedErrorMessage = "'email' should have a valid format";
        final var expectedErrorCount = 1;

        final var actualInput = new CreateCustomerUseCase.Input(expectedName, expectedEmail);

        // when
        final var actualException = assertThrows(NotificationException.class, () -> this.useCase.execute(actualInput));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }
}