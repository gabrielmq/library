package io.github.gabrielmsouza.library.infrastructure.api;

import io.github.gabrielmsouza.library.infrastructure.customer.models.CreateCustomerRequest;
import io.github.gabrielmsouza.library.infrastructure.customer.models.CustomerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Customers")
@RequestMapping("/customers")
public interface CustomerAPI {
    @PostMapping(
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created successfully"),
        @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> create(@RequestBody @Valid CreateCustomerRequest request);

    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a customer by it's identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer retrieved"),
        @ApiResponse(responseCode = "404", description = "Customer was not found"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<CustomerResponse> getById(@PathVariable String id);
}
