package io.github.gabrielmsouza.library.domain.book;

public record LoansByBookSearchQuery(
        int page,
        int perPage,
        String id,
        String sort,
        String direction
) {
}
