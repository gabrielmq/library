package io.github.gabrielmsouza.library.infrastructure.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.application.book.ListBooksUseCase;
import io.github.gabrielmsouza.library.application.loan.CreateLoanUseCase;
import io.github.gabrielmsouza.library.application.loan.ListLoansUseCase;
import io.github.gabrielmsouza.library.application.loan.ReturnLoanUseCase;
import io.github.gabrielmsouza.library.domain.book.Book;
import io.github.gabrielmsouza.library.domain.exceptions.NotFoundException;
import io.github.gabrielmsouza.library.domain.exceptions.NotificationException;
import io.github.gabrielmsouza.library.domain.loan.Loan;
import io.github.gabrielmsouza.library.domain.loan.LoanSearchQuery;
import io.github.gabrielmsouza.library.domain.pagination.Pagination;
import io.github.gabrielmsouza.library.domain.pagination.SearchQuery;
import io.github.gabrielmsouza.library.domain.validation.Error;
import io.github.gabrielmsouza.library.domain.validation.handler.Notification;
import io.github.gabrielmsouza.library.infrastructure.ControllerTest;
import io.github.gabrielmsouza.library.infrastructure.api.LoanAPI;
import io.github.gabrielmsouza.library.infrastructure.loan.models.CreateLoanRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = LoanAPI.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateLoanUseCase createLoanUseCase;

    @MockBean
    private ReturnLoanUseCase returnLoanUseCase;

    @MockBean
    private ListLoansUseCase listLoansUseCase;

    @Test
    void givenAValidRequest_whenCallsCreateLoan_thenShouldReturnLoanId() throws Exception {
        // given
        final var expectedId = Fixture.randomId();
        final var expectedIsbn = Fixture.Book.isbn();
        final var expectedCustomerId = Fixture.randomId();

        when(createLoanUseCase.execute(any())).thenReturn(CreateLoanUseCase.Output.from(expectedId));

        final var expectedRequest = new CreateLoanRequest(expectedIsbn, expectedCustomerId);

        final var request = post("/loans")
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(expectedRequest));

        // when
        final var response = this.mvc.perform(request);

        // then
        response
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "%s/%s".formatted("/loans", expectedId)))
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId)));

        final var captor = ArgumentCaptor.forClass(CreateLoanUseCase.Input.class);
        verify(createLoanUseCase).execute(captor.capture());

        final var input = captor.getValue();
        assertEquals(expectedIsbn, input.isbn());
        assertEquals(expectedCustomerId, input.customerId());
    }

    @Test
    void givenAValidRequest_whenCallsCreateLoanAndBookDoesNotExists_thenShouldReturnNotFound() throws Exception {
        // given
        final var expectedIsbn = Fixture.Book.isbn();
        final var expectedCustomerId = Fixture.randomId();

        String expectedErrorMessage = "Book with ISBN %s was not found".formatted(expectedIsbn);

        when(createLoanUseCase.execute(any()))
                .thenThrow(NotFoundException.with(expectedErrorMessage));

        final var expectedRequest = new CreateLoanRequest(expectedIsbn, expectedCustomerId);

        final var request = post("/loans")
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(expectedRequest));

        // when
        final var response = this.mvc.perform(request);

        // then
        response
            .andExpect(status().isNotFound())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    void givenAValidRequest_whenCallsCreateLoanAndCustomerDoesNotExists_thenShouldReturnNotFound() throws Exception {
        // given
        final var expectedIsbn = Fixture.Book.isbn();
        final var expectedCustomerId = Fixture.randomId();

        String expectedErrorMessage = "Customer with ID %s was not found".formatted(expectedCustomerId);

        when(createLoanUseCase.execute(any()))
                .thenThrow(NotFoundException.with(expectedErrorMessage));

        final var expectedRequest = new CreateLoanRequest(expectedIsbn, expectedCustomerId);

        final var request = post("/loans")
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(expectedRequest));

        // when
        final var response = this.mvc.perform(request);

        // then
        response
            .andExpect(status().isNotFound())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    void givenAValidRequest_whenCallsCreateLoanAndBookIsOnLoan_thenShouldThrowsAnError() throws Exception {
        // given
        final var expectedIsbn = Fixture.Book.isbn();
        final var expectedCustomerId = Fixture.randomId();

        final var expectedErrorMessage = "Book with ID 123 is already on loan";
        final var expectedErrorSize = 1;

        when(this.createLoanUseCase.execute(any()))
            .thenThrow(NotificationException.with(expectedErrorMessage, Notification.create(Error.with(expectedErrorMessage))));

        final var expectedRequest = new CreateLoanRequest(expectedIsbn, expectedCustomerId);

        final var request = post("/loans")
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(expectedRequest));

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
    void givenAValidLoanId_whenCallsReturnLoan_thenShouldBeOk() throws Exception {
        // given
        final var expectedId = Fixture.randomId();

        final var request = patch("/loans/%s/returned".formatted(expectedId))
            .contentType(APPLICATION_JSON);

        // when
        final var response = this.mvc.perform(request);

        // then
        response.andExpect(status().isOk());

        verify(this.returnLoanUseCase).execute(expectedId);
    }

    @Test
    void givenAInvalidLoanId_whenCallsReturnLoanAndLoanDoesNotExists_thenShouldReturnNotFound() throws Exception {
        // given
        final var expectedId = Fixture.randomId();
        final var expectedErrorMessage = "Loan with ID %s was not found".formatted(expectedId);

        doThrow(NotFoundException.with(expectedErrorMessage))
                .when(this.returnLoanUseCase).execute(expectedId);

        final var request = patch("/loans/%s/returned".formatted(expectedId))
            .contentType(APPLICATION_JSON);

        // when
        final var response = this.mvc.perform(request);

        // then
        response
            .andExpect(status().isNotFound())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    void givenValidParams_whenCallsListBooks_thenShouldReturnIt() throws Exception {
        // given
        final var book = Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn());

        final var loan = Loan.with(Fixture.randomId(), book.getId());

        final var expectedPage = 1;
        final var expectedPerPage = 10;
        final var expectedIsbn = "";
        final var expectedCustomerId = "";
        final var expectedSort = "loan_date";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(ListLoansUseCase.Output.from(loan));

        when(this.listLoansUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = get("/loans")
                .accept(APPLICATION_JSON)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("per_page", String.valueOf(expectedPerPage))
                .queryParam("isbn", expectedIsbn)
                .queryParam("customer_id", expectedCustomerId)
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
            .andExpect(jsonPath("$.items[0].id", equalTo(loan.getId())))
            .andExpect(jsonPath("$.items[0].customer_id", equalTo(loan.getCustomerId())))
            .andExpect(jsonPath("$.items[0].book_id", equalTo(loan.getBookId())))
            .andExpect(jsonPath("$.items[0].loan_date", equalTo(loan.getLoanDate().toString())))
            .andExpect(jsonPath("$.items[0].returned", equalTo(loan.isReturned())));

        final var captor = ArgumentCaptor.forClass(LoanSearchQuery.class);
        verify(this.listLoansUseCase, times(1)).execute(captor.capture());

        final var query = captor.getValue();
        assertEquals(expectedPage, query.page());
        assertEquals(expectedPerPage, query.perPage());
        assertEquals(expectedIsbn, query.isbn());
        assertEquals(expectedCustomerId, query.customerId());
        assertEquals(expectedSort, query.sort());
        assertEquals(expectedDirection, query.direction());
    }

    @Test
    void givenEmptyParams_whenCallsListBooks_thenShouldUseDefaultsAndReturnIt() throws Exception {
        // given
        final var book = Book.with(Fixture.Book.title(), Fixture.Book.author(), Fixture.Book.isbn());

        final var loan = Loan.with(Fixture.randomId(), book.getId());

        final var expectedPage = 1;
        final var expectedPerPage = 10;
        final var expectedIsbn = "";
        final var expectedCustomerId = "";
        final var expectedSort = "loan_date";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(ListLoansUseCase.Output.from(loan));

        when(this.listLoansUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));


        final var request = get("/loans")
                .accept(APPLICATION_JSON)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("per_page", String.valueOf(expectedPerPage))
                .queryParam("isbn", expectedIsbn)
                .queryParam("customer_id", expectedCustomerId)
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
            .andExpect(jsonPath("$.items[0].id", equalTo(loan.getId())))
            .andExpect(jsonPath("$.items[0].customer_id", equalTo(loan.getCustomerId())))
            .andExpect(jsonPath("$.items[0].book_id", equalTo(loan.getBookId())))
            .andExpect(jsonPath("$.items[0].loan_date", equalTo(loan.getLoanDate().toString())))
            .andExpect(jsonPath("$.items[0].returned", equalTo(loan.isReturned())));

        final var captor = ArgumentCaptor.forClass(LoanSearchQuery.class);
        verify(this.listLoansUseCase, times(1)).execute(captor.capture());

        final var query = captor.getValue();
        assertEquals(expectedPage, query.page());
        assertEquals(expectedPerPage, query.perPage());
        assertEquals(expectedIsbn, query.isbn());
        assertEquals(expectedCustomerId, query.customerId());
        assertEquals(expectedSort, query.sort());
        assertEquals(expectedDirection, query.direction());
    }
}