package io.github.gabrielmsouza.library.infrastructure.api;

import io.github.gabrielmsouza.library.domain.pagination.Pagination;
import io.github.gabrielmsouza.library.infrastructure.book.models.BookResponse;
import io.github.gabrielmsouza.library.infrastructure.book.models.CreateBookRequest;
import io.github.gabrielmsouza.library.infrastructure.book.models.ListBooksResponse;
import io.github.gabrielmsouza.library.infrastructure.book.models.UpdateBookRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Books")
@RequestMapping("/books")
public interface BookAPI {
    @PostMapping(
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new book")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created successfully"),
        @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> create(@RequestBody @Valid CreateBookRequest request);

    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a book by it's identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book retrieved"),
        @ApiResponse(responseCode = "404", description = "Book was not found"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<BookResponse> getById(@PathVariable String id);

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(value = "{id}")
    @Operation(summary = "Delete a book by it's identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Book deleted"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    void deleteById(@PathVariable String id);

    @PutMapping(
        value = "{id}",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a book by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated"),
            @ApiResponse(responseCode = "404", description = "Book was not found"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> updateById(@PathVariable String id, @RequestBody UpdateBookRequest request);

    @GetMapping
    @Operation(summary = "List all books")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Books retrieved"),
        @ApiResponse(responseCode = "400", description = "An invalid parameter was received"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    Pagination<ListBooksResponse> list(
        @RequestParam(name = "search", required = false, defaultValue = "") String search,
        @RequestParam(name = "page", required = false, defaultValue = "0") int page,
        @RequestParam(name = "per_page", required = false, defaultValue = "10") int perPage,
        @RequestParam(name = "sort", required = false, defaultValue = "isbn") String sort,
        @RequestParam(name = "dir", required = false, defaultValue = "asc") String direction
    );
}
