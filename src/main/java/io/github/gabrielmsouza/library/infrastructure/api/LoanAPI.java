package io.github.gabrielmsouza.library.infrastructure.api;

import io.github.gabrielmsouza.library.domain.pagination.Pagination;
import io.github.gabrielmsouza.library.infrastructure.loan.models.CreateLoanRequest;
import io.github.gabrielmsouza.library.infrastructure.loan.models.ListLoansResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Loans")
@RequestMapping("/loans")
public interface LoanAPI {
    @PostMapping(
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new loan")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created successfully"),
        @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> create(@RequestBody @Valid CreateLoanRequest request);

    @ResponseStatus(OK)
    @PatchMapping(value = "/{id}/returned")
    @Operation(summary = "Return a loaned book")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returned successfully"),
        @ApiResponse(responseCode = "404", description = "Loan was not found"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    void returned(@PathVariable String id);

    @GetMapping
    @Operation(summary = "List all loans")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Loans retrieved"),
        @ApiResponse(responseCode = "400", description = "An invalid parameter was received"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    Pagination<ListLoansResponse> list(
            @RequestParam(name = "isbn", required = false, defaultValue = "") String isbn,
            @RequestParam(name = "customer_id", required = false, defaultValue = "") String customerId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "per_page", required = false, defaultValue = "10") int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "loanDate") String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String direction
    );
}
