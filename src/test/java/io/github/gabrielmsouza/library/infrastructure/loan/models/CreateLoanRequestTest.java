package io.github.gabrielmsouza.library.infrastructure.loan.models;

import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.infrastructure.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;


@JacksonTest
class CreateLoanRequestTest {

    @Autowired
    private JacksonTester<CreateLoanRequest> json;

    @Test
    void testUnmarshall() throws Exception {
        // given
        final var expectedIsbn = Fixture.Book.isbn();
        final var expectedCustomerId = Fixture.randomId();

        final var json = """
        {
          "customer_id": "%s",
          "isbn": "%s"
        }
        """.formatted(expectedCustomerId, expectedIsbn);

        // when
        final var actualJson = this.json.parse(json);

        // then
        assertThat(actualJson)
            .hasFieldOrPropertyWithValue("customerId", expectedCustomerId)
            .hasFieldOrPropertyWithValue("isbn", expectedIsbn);
    }
}