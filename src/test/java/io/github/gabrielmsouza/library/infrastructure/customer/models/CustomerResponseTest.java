package io.github.gabrielmsouza.library.infrastructure.customer.models;

import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.infrastructure.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JacksonTest
class CustomerResponseTest {
    @Autowired
    private JacksonTester<CustomerResponse> json;

    @Test
    void testCustomerResponseMarshall() throws Exception {
        // given
        final var expectedId = Fixture.randomId();
        final var expectedName = Fixture.Customer.name();
        final var expectedEmail = Fixture.Customer.email();

        final var response = new CustomerResponse(
            expectedId,
            expectedName,
            expectedEmail
        );

        // when
        final var actualJson = this.json.write(response);

        // then
        assertThat(actualJson)
            .hasJsonPathValue("$.id", expectedId)
            .hasJsonPathValue("$.name", expectedName)
            .hasJsonPathValue("$.email", expectedEmail);
    }
}