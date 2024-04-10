package io.github.gabrielmsouza.library.domain.customer;

import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.domain.UnitTest;
import io.github.gabrielmsouza.library.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest extends UnitTest {

    @Test
    void givenValidParams_whenCallsCreateCustomer_thenShouldInstantiateACustomer() {
        // given
        final var expectedName = Fixture.Customer.name();
        final var expectedEmail = Fixture.Customer.email();

        // when
        final var actualCustomer = Customer.with(expectedName, expectedEmail);

        // then
        assertNotNull(actualCustomer);
        assertNotNull(actualCustomer.getId());
        assertEquals(expectedName, actualCustomer.getName());
        assertEquals(expectedEmail, actualCustomer.getEmail());
    }

    @Test
    void givenAInvalidNullName_whenCallsCreateCustomer_thenShouldReceiveANotification() {
        // given
        final var expectedEmail = Fixture.Customer.email();

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        // when
        final var actualException = assertThrows(NotificationException.class, () -> Customer.with(null, expectedEmail));

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidEmptyName_whenCallsCreateCustomer_thenShouldReceiveANotification() {
        // given
        final var expectedEmail = Fixture.Customer.email();

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        // when
        final var actualException = assertThrows(NotificationException.class, () -> Customer.with("", expectedEmail));

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidNullEmail_whenCallsCreateCustomer_thenShouldReceiveANotification() {
        // given
        final var expectedName = Fixture.Customer.name();

        final var expectedErrorMessage = "'email' should have a valid format";
        final var expectedErrorCount = 1;

        // when
        final var actualException = assertThrows(NotificationException.class, () -> Customer.with(expectedName, null));

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidEmptyEmail_whenCallsCreateCustomer_thenShouldReceiveANotification() {
        // given
        final var expectedName = Fixture.Customer.name();

        final var expectedErrorMessage = "'email' should have a valid format";
        final var expectedErrorCount = 1;

        // when
        final var actualException = assertThrows(NotificationException.class, () -> Customer.with(expectedName, ""));

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    void givenAInvalidEmail_whenCallsCreateCustomer_thenShouldReceiveANotification() {
        // given
        final var expectedName = Fixture.Customer.name();
        final var expectedEmail = "invalid-email";

        final var expectedErrorMessage = "'email' should have a valid format";
        final var expectedErrorCount = 1;

        // when
        final var actualException = assertThrows(NotificationException.class, () -> Customer.with(expectedName, expectedEmail));

        // then
        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }
}