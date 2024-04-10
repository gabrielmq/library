package io.github.gabrielmsouza.library.infrastructure.customer.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CustomerResponse(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("email") String email
) {}
