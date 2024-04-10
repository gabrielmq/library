package io.github.gabrielmsouza.library.infrastructure.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.application.book.*;
import io.github.gabrielmsouza.library.application.book.CreateBookUseCase.Output;
import io.github.gabrielmsouza.library.domain.book.Book;
import io.github.gabrielmsouza.library.domain.exceptions.NotFoundException;
import io.github.gabrielmsouza.library.domain.exceptions.NotificationException;
import io.github.gabrielmsouza.library.domain.pagination.Pagination;
import io.github.gabrielmsouza.library.domain.pagination.SearchQuery;
import io.github.gabrielmsouza.library.domain.validation.Error;
import io.github.gabrielmsouza.library.domain.validation.handler.Notification;
import io.github.gabrielmsouza.library.infrastructure.ControllerTest;
import io.github.gabrielmsouza.library.infrastructure.api.BookAPI;
import io.github.gabrielmsouza.library.infrastructure.book.models.CreateBookRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = BookAPI.class)
class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateBookUseCase createBookUseCase;

    @MockBean
    private GetBookByIdUseCase getBookByIdUseCase;

    @MockBean
    private DeleteBookUseCase deleteBookUseCase;

    @MockBean
    private UpdateBookUseCase updateBookUseCase;

    @MockBean
    private ListBooksUseCase listBooksUseCase;

    @Test
    void givenAValidRequest_whenCallsCreateBook_thenShouldReturnBookId() throws Exception {
        // given
        final var expectedId = Fixture.randomId();
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        when(createBookUseCase.execute(any())).thenReturn(Output.from(expectedId));

        final var request = post("/books")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(
                    new CreateBookRequest(
                        expectedTitle,
                        expectedAuthor,
                        expectedIsbn
                )));

        // when
        final var response = this.mvc.perform(request);

        // then
        response
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "%s/%s".formatted("/books", expectedId)))
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId)));

        final var captor = ArgumentCaptor.forClass(CreateBookUseCase.Input.class);
        verify(createBookUseCase, times(1)).execute(captor.capture());

        final var input = captor.getValue();
        assertEquals(expectedTitle, input.title());
        assertEquals(expectedAuthor, input.author());
        assertEquals(expectedIsbn, input.isbn());
    }

    @Test
    void givenAnInvalidRequest_whenCallsCreateBook_thenShouldThrowsAnError() throws Exception {
        // given
        final String expectedTitle = null;
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorSize = 1;

        when(createBookUseCase.execute(any()))
            .thenThrow(new NotificationException("Error", Notification.create(Error.with(expectedErrorMessage))));

        final var request = post("/books")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(
                        new CreateBookRequest(
                                expectedTitle,
                                expectedAuthor,
                                expectedIsbn
                        )));

        // when
        final var response = this.mvc.perform(request);

        // then
        response
            .andExpect(status().isUnprocessableEntity())
            .andExpect(header().string("Location", nullValue()))
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.errors", hasSize(expectedErrorSize)))
            .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }

    @Test
    void givenAValidRequest_whenCallsCreateBookAndAlreadyExistsABookWithSameISBN_thenShouldThrowsAnError() throws Exception {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedErrorMessage = "Already exists a book with isbn %s".formatted(expectedIsbn);
        final var expectedErrorSize = 1;

        when(createBookUseCase.execute(any()))
            .thenThrow(NotificationException.with("Error", Notification.create(Error.with(expectedErrorMessage))));

        final var request = post("/books")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(
                        new CreateBookRequest(
                                expectedTitle,
                                expectedAuthor,
                                expectedIsbn
                        )));

        // when
        final var response = this.mvc.perform(request);

        // then
        response
            .andExpect(status().isUnprocessableEntity())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.errors", hasSize(expectedErrorSize)))
            .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }

    @Test
    void givenAValidId_whenCallsGetById_thenShouldReturnBook() throws Exception {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var book = Book.with(expectedTitle, expectedAuthor, expectedIsbn);

        final var expectedId = book.getId();

        when(getBookByIdUseCase.execute(any())).thenReturn(GetBookByIdUseCase.Output.from(book));


        final var request = get("/books/{id}", expectedId)
            .accept(APPLICATION_JSON);

        // when
        final var response = this.mvc.perform(request);

        // then
        response
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId)))
            .andExpect(jsonPath("$.title", equalTo(expectedTitle)))
            .andExpect(jsonPath("$.author", equalTo(expectedAuthor)))
            .andExpect(jsonPath("$.isbn", equalTo(expectedIsbn)));

        verify(getBookByIdUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    void givenAnInvalidId_whenCallsGetByIdAndCastMemberDoesNotExists_thenShouldNotFound() throws Exception {
        // given
        final var expectedId = Fixture.randomId();
        final var expectedErrorMessage = "Book with ID %s was not found".formatted(expectedId);

        when(getBookByIdUseCase.execute(any()))
            .thenThrow(NotFoundException.with(Book.class, expectedId));

        final var request = get("/books/{id}", expectedId)
            .accept(APPLICATION_JSON);

        // when
        final var response = this.mvc.perform(request);

        // then
        response
            .andExpect(status().isNotFound())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(getBookByIdUseCase).execute(eq(expectedId));
    }

    @Test
    void givenAValidId_whenCallsDeleteById_thenShouldDeleteIt() throws Exception {
        // given
        final var expectedId = Fixture.randomId();

        doNothing().when(this.deleteBookUseCase).execute(any());

        final var request = delete("/books/{id}", expectedId)
                .accept(APPLICATION_JSON);

        // when
        final var response = this.mvc.perform(request);

        // then
        response.andExpect(status().isNoContent());
    }

    @Test
    void givenAValidRequest_whenCallsUpdateBook_thenShouldReturnBookId() throws Exception {
        // given
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var book = Book.with(expectedTitle, expectedAuthor, expectedIsbn);
        final var expectedId = book.getId();

        final var expectedRequestBody = new CreateBookRequest(expectedTitle, expectedAuthor, expectedIsbn);

        when(updateBookUseCase.execute(any())).thenReturn(UpdateBookUseCase.Output.from(expectedId));

        final var request = put("/books/{id}", expectedId)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(expectedRequestBody));

        // when
        final var response = this.mvc.perform(request);

        // then
        response
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId)));

        final var captor = ArgumentCaptor.forClass(UpdateBookUseCase.Input.class);
        verify(updateBookUseCase, times(1)).execute(captor.capture());

        final var input = captor.getValue();
        assertEquals(expectedId, input.id());
        assertEquals(expectedTitle, input.title());
        assertEquals(expectedAuthor, input.author());
        assertEquals(expectedIsbn, input.isbn());
    }

    @Test
    void givenAnInvalidTitle_whenCallsUpdateBook_thenShouldThrowsAnError() throws Exception {
        // given
        final var book = Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn());

        final var expectedId = Fixture.randomId();
        final String expectedTitle = null;
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedRequestBody = new CreateBookRequest(expectedTitle, expectedAuthor, expectedIsbn);

        final var expectedErrorSize = 1;
        final var expectedErrorMessage = "'title' should not be empty";

        when(updateBookUseCase.execute(any()))
            .thenThrow(new NotificationException("Error", Notification.create(Error.with(expectedErrorMessage))));

        final var request = put("/books/{id}", expectedId)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(expectedRequestBody));

        // when
        final var response = this.mvc.perform(request);

        // then
        response
            .andExpect(status().isUnprocessableEntity())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.errors", hasSize(expectedErrorSize)))
            .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        final var captor = ArgumentCaptor.forClass(UpdateBookUseCase.Input.class);
        verify(updateBookUseCase, times(1)).execute(captor.capture());

        final var input = captor.getValue();
        assertEquals(expectedId, input.id());
        assertEquals(expectedTitle, input.title());
        assertEquals(expectedAuthor, input.author());
        assertEquals(expectedIsbn, input.isbn());
    }

    @Test
    void givenAValidId_whenCallsUpdateBookAndBookDoesNotExists_thenShouldReturnNotFound() throws Exception {
        // given
        final var expectedId = Fixture.randomId();
        final var expectedTitle = Fixture.Book.title();
        final var expectedAuthor = Fixture.Book.author();
        final var expectedIsbn = Fixture.Book.isbn();

        final var expectedErrorMessage = "Book with ID %s was not found".formatted(expectedId);

        final var expectedRequestBody = new CreateBookRequest(
                expectedTitle,
                expectedAuthor,
                expectedIsbn
        );

        when(updateBookUseCase.execute(any()))
            .thenThrow(NotFoundException.with(Book.class, expectedId));

        final var request = put("/books/{id}", expectedId)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(expectedRequestBody));

        // when
        final var response = this.mvc.perform(request);

        // then
        response
            .andExpect(status().isNotFound())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        final var captor = ArgumentCaptor.forClass(UpdateBookUseCase.Input.class);
        verify(updateBookUseCase, times(1)).execute(captor.capture());

        final var input = captor.getValue();
        assertEquals(expectedId, input.id());
        assertEquals(expectedTitle, input.title());
        assertEquals(expectedAuthor, input.author());
        assertEquals(expectedIsbn, input.isbn());
    }

    @Test
    void givenValidParams_whenCallsListBooks_thenShouldReturnIt() throws Exception {
        // given
        final var book = Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn());

        final var expectedPage = 1;
        final var expectedPerPage = 10;
        final var expectedTerms = book.getTitle();
        final var expectedSort = "isbn";
        final var expectedDirection = "desc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(ListBooksUseCase.Output.from(book));

        when(listBooksUseCase.execute(any()))
            .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = get("/books")
            .accept(APPLICATION_JSON)
            .queryParam("page", String.valueOf(expectedPage))
            .queryParam("per_page", String.valueOf(expectedPerPage))
            .queryParam("search", expectedTerms)
            .queryParam("sort", expectedSort)
            .queryParam("dir", expectedDirection);

        // when
        final var response = this.mvc.perform(request);

        // then
        response
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
            .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
            .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
            .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
            .andExpect(jsonPath("$.items[0].id", equalTo(book.getId())))
            .andExpect(jsonPath("$.items[0].title", equalTo(book.getTitle())))
            .andExpect(jsonPath("$.items[0].author", equalTo(book.getAuthor())))
            .andExpect(jsonPath("$.items[0].isbn", equalTo(book.getIsbn())));

        final var captor = ArgumentCaptor.forClass(SearchQuery.class);
        verify(listBooksUseCase, times(1)).execute(captor.capture());

        final var query = captor.getValue();
        assertEquals(expectedPage, query.page());
        assertEquals(expectedPerPage, query.perPage());
        assertEquals(expectedTerms, query.terms());
        assertEquals(expectedSort, query.sort());
        assertEquals(expectedDirection, query.direction());
    }

    @Test
    void givenEmptyParams_whenCallsListBooks_thenShouldUseDefaultsAndReturnIt() throws Exception {
        // given
        final var book = Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn());

        final var expectedPage = 1;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "isbn";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(ListBooksUseCase.Output.from(book));

        when(listBooksUseCase.execute(any()))
            .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = get("/books")
            .accept(APPLICATION_JSON)
            .queryParam("page", String.valueOf(expectedPage))
            .queryParam("per_page", String.valueOf(expectedPerPage))
            .queryParam("search", expectedTerms)
            .queryParam("sort", expectedSort)
            .queryParam("dir", expectedDirection);

        // when
        final var response = this.mvc.perform(request);

        // then
        response
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
            .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
            .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
            .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
            .andExpect(jsonPath("$.items[0].id", equalTo(book.getId())))
            .andExpect(jsonPath("$.items[0].title", equalTo(book.getTitle())))
            .andExpect(jsonPath("$.items[0].author", equalTo(book.getAuthor())))
            .andExpect(jsonPath("$.items[0].isbn", equalTo(book.getIsbn())));

        final var captor = ArgumentCaptor.forClass(SearchQuery.class);
        verify(listBooksUseCase, times(1)).execute(captor.capture());

        final var query = captor.getValue();
        assertEquals(expectedPage, query.page());
        assertEquals(expectedPerPage, query.perPage());
        assertEquals(expectedTerms, query.terms());
        assertEquals(expectedSort, query.sort());
        assertEquals(expectedDirection, query.direction());
    }
}
