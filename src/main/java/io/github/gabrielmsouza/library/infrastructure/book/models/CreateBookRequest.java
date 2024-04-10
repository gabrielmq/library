package io.github.gabrielmsouza.library.infrastructure.book.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateBookRequest(
        @JsonProperty("title") String title,
        @JsonProperty("author") String author,
        @JsonProperty("isbn") String isbn
) {
}
