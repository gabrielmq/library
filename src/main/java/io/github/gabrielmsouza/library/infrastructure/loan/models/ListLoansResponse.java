package io.github.gabrielmsouza.library.infrastructure.loan.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record ListLoansResponse(
        @JsonProperty("id") String id,
        @JsonProperty("book_id") String bookId,
        @JsonProperty("customer_id") String customerId,
        @JsonProperty("loan_date") Instant loanDate,
        @JsonProperty("return_date") Instant returnDate,
        @JsonProperty("returned") boolean returned
) {
}
