package io.github.gabrielmsouza.library.infrastructure.loan.models;

import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.infrastructure.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@JacksonTest
class ListLoansResponseTest {
    @Autowired
    private JacksonTester<ListLoansResponse> json;

    @Test
    void testListLoansResponseMarshall() throws Exception {
        // given
        final var expectedId = Fixture.randomId();
        final var expectedBookId = Fixture.randomId();
        final var expectedCustomerId = Fixture.randomId();
        final var expectedLoanDate = Instant.now();
        final var expectedReturnDate = Instant.now();
        final var expectedReturned = false;

        final var response = new ListLoansResponse(
            expectedId,
            expectedBookId,
            expectedCustomerId,
            expectedLoanDate,
            expectedReturnDate,
            expectedReturned
        );

        // when
        final var actualJson = this.json.write(response);

        // then
        assertThat(actualJson)
            .hasJsonPathValue("$.id", expectedId)
            .hasJsonPathValue("$.book_id", expectedBookId)
            .hasJsonPathValue("$.customer_id", expectedCustomerId)
            .hasJsonPathValue("$.loan_date", expectedLoanDate.toString())
            .hasJsonPathValue("$.return_date", expectedReturnDate.toString())
            .hasJsonPathValue("$.returned", expectedReturned);
    }
}