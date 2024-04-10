package io.github.gabrielmsouza.library.domain.loan;

public record LoanSearchQuery(
    int page,
    int perPage,
    String isbn,
    String customerId,
    String sort,
    String direction
) {
}
