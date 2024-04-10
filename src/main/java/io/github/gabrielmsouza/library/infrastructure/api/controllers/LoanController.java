package io.github.gabrielmsouza.library.infrastructure.api.controllers;

import io.github.gabrielmsouza.library.application.loan.CreateLoanUseCase;
import io.github.gabrielmsouza.library.application.loan.ListLoansUseCase;
import io.github.gabrielmsouza.library.application.loan.ReturnLoanUseCase;
import io.github.gabrielmsouza.library.domain.loan.LoanSearchQuery;
import io.github.gabrielmsouza.library.domain.pagination.Pagination;
import io.github.gabrielmsouza.library.infrastructure.api.LoanAPI;
import io.github.gabrielmsouza.library.infrastructure.loan.models.CreateLoanRequest;
import io.github.gabrielmsouza.library.infrastructure.loan.models.ListLoansResponse;
import io.github.gabrielmsouza.library.infrastructure.loan.present.LoanPresent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class LoanController implements LoanAPI {
    private final CreateLoanUseCase createLoanUseCase;
    private final ReturnLoanUseCase returnLoanUseCase;
    private final ListLoansUseCase listLoansUseCase;

    public LoanController(
            final CreateLoanUseCase createLoanUseCase,
            final ReturnLoanUseCase returnLoanUseCase,
            final ListLoansUseCase listLoansUseCase
    ) {
        this.createLoanUseCase = Objects.requireNonNull(createLoanUseCase);
        this.returnLoanUseCase = Objects.requireNonNull(returnLoanUseCase);
        this.listLoansUseCase = Objects.requireNonNull(listLoansUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateLoanRequest request) {
        final var output = this.createLoanUseCase.execute(CreateLoanUseCase.Input.from(request));
        return ResponseEntity
                .created(URI.create("/loans/%s".formatted(output.id())))
                .body(output);
    }

    @Override
    public void returned(final String id) {
        this.returnLoanUseCase.execute(id);
    }

    @Override
    public Pagination<ListLoansResponse> list(
            final String isbn,
            final String customerId,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        final var query = new LoanSearchQuery(
                page,
                perPage,
                isbn,
                customerId,
                sort,
                direction
        );
        return this.listLoansUseCase.execute(query).map(LoanPresent::present);
    }
}
