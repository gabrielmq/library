package io.github.gabrielmsouza.library.infrastructure.loan.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateLoanRequest(
    @JsonProperty("isbn") String isbn,
    @JsonProperty("customer_id") String customerId
) {}
