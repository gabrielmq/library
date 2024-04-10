package io.github.gabrielmsouza.library.infrastructure.book.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ListBooksResponse(
    @JsonProperty("id") String id,
    @JsonProperty("title") String title,
    @JsonProperty("author") String author,
    @JsonProperty("isbn") String isbn
) {
}
