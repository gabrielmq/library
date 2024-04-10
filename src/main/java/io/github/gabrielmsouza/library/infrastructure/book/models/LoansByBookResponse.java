package io.github.gabrielmsouza.library.infrastructure.book.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record LoansByBookResponse(
        @JsonProperty("id") String id,
        @JsonProperty("title") String title,
        @JsonProperty("author") String author,
        @JsonProperty("loans") List<String> loans
) {
}
