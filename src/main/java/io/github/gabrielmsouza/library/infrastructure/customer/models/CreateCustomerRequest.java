package io.github.gabrielmsouza.library.infrastructure.customer.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCustomerRequest(
    @JsonProperty("name") String name,
    @JsonProperty("email") String email
) {
}
