package io.github.gabrielmsouza.library.infrastructure.customer.models;

import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.infrastructure.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JacksonTest
class CreateCustomerRequestTest {
    @Autowired
    private JacksonTester<CreateCustomerRequest> json;

    @Test
    void testCreateCustomerRequestUnmarshall() throws Exception {
        // given
        final var expectedName = Fixture.Customer.name();
        final var expectedEmail = Fixture.Customer.email();

        // when
        final var json = """
            {
              "name": "%s",
              "email": "%s"
            }
            """.formatted(expectedName, expectedEmail);

        // then
        final var actualJson = this.json.parse(json);

        assertThat(actualJson)
            .hasFieldOrPropertyWithValue("name", expectedName)
            .hasFieldOrPropertyWithValue("email", expectedEmail);
    }
}