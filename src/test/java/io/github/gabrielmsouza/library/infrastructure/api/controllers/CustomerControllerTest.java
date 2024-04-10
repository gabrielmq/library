package io.github.gabrielmsouza.library.infrastructure.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.application.customer.CreateCustomerUseCase;
import io.github.gabrielmsouza.library.application.customer.GetCustomerByIdUseCase;
import io.github.gabrielmsouza.library.domain.book.Book;
import io.github.gabrielmsouza.library.domain.customer.Customer;
import io.github.gabrielmsouza.library.domain.exceptions.NotFoundException;
import io.github.gabrielmsouza.library.domain.exceptions.NotificationException;
import io.github.gabrielmsouza.library.domain.validation.Error;
import io.github.gabrielmsouza.library.domain.validation.handler.Notification;
import io.github.gabrielmsouza.library.infrastructure.ControllerTest;
import io.github.gabrielmsouza.library.infrastructure.api.CustomerAPI;
import io.github.gabrielmsouza.library.infrastructure.customer.models.CreateCustomerRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CustomerAPI.class)
class CustomerControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCustomerUseCase createCustomerUseCase;

    @MockBean
    private GetCustomerByIdUseCase getCustomerUseCase;

    @Test
    void givenAValidRequest_whenCallsCreateCustomer_thenShouldReturnCustomerId() throws Exception {
        // given
        final var expectedId = Fixture.randomId();
        final var expectedName = Fixture.Customer.name();
        final var expectedEmail = Fixture.Customer.email();

        final var actualRequestBody = new CreateCustomerRequest(expectedName, expectedEmail);
        final var actualRequest = post("/customers")
                .contentType(APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(actualRequestBody));

        when(this.createCustomerUseCase.execute(any()))
                .thenReturn(CreateCustomerUseCase.Output.from(expectedId));

        // when
        final var actualResponse = this.mvc.perform(actualRequest);

        // then
        actualResponse
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "%s/%s".formatted("/customers", expectedId)))
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId)));

        final var captor = ArgumentCaptor.forClass(CreateCustomerUseCase.Input.class);
        verify(this.createCustomerUseCase).execute(captor.capture());

        final var actualInput = captor.getValue();
        assertEquals(expectedName, actualInput.name());
        assertEquals(expectedEmail, actualInput.email());
    }

    @Test
    void givenAInvalidRequest_whenCallsCreateCustomer_thenShouldThrowsAnError() throws Exception {
        // given
        final var expectedNameErrorMessage = "'name' should not be empty";
        final var expectedEmailErrorMessage = "'email' should have a valid format";
        final var expectedErrorSize = 2;

        final var actualRequestBody = new CreateCustomerRequest(null, null);
        final var actualRequest = post("/customers")
                .contentType(APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(actualRequestBody));

        final var notification = Notification.create()
                .append(Error.with(expectedNameErrorMessage))
                .append(Error.with(expectedEmailErrorMessage));

        when(this.createCustomerUseCase.execute(any()))
            .thenThrow(NotificationException.with("Error", notification));


        // when
        final var actualResponse = this.mvc.perform(actualRequest);

        // then
        actualResponse
            .andExpect(status().isUnprocessableEntity())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.errors", hasSize(expectedErrorSize)))
            .andExpect(jsonPath("$.errors[0].message", equalTo(expectedNameErrorMessage)))
            .andExpect(jsonPath("$.errors[1].message", equalTo(expectedEmailErrorMessage)));
    }

    @Test
    void givenAValidId_whenCallsGetById_thenShouldReturnCustomer() throws Exception {
        // given
        final var expectedName = Fixture.Customer.name();
        final var expectedEmail = Fixture.Customer.email();

        final var customer = Customer.with(expectedName, expectedEmail);
        final var expectedId = customer.getId();

        final var actualRequest = get("/customers/{id}", expectedId)
                .contentType(APPLICATION_JSON);

        when(this.getCustomerUseCase.execute(any()))
                .thenReturn(GetCustomerByIdUseCase.Output.from(customer));

        // when
        final var actualResponse = this.mvc.perform(actualRequest);

        // then
        actualResponse
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId)))
            .andExpect(jsonPath("$.name", equalTo(expectedName)))
            .andExpect(jsonPath("$.email", equalTo(expectedEmail)));

        verify(this.getCustomerUseCase).execute(expectedId);
    }

    @Test
    void givenAnInvalidId_whenCallsGetById_thenShouldReturnNotFound() throws Exception {
        // given
        final var expectedId = Fixture.randomId();
        final var expectedErrorMessage = "Customer with ID %s was not found".formatted(expectedId);

        final var actualRequest = get("/customers/{id}", expectedId)
                .contentType(APPLICATION_JSON);

        when(this.getCustomerUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Customer.class, expectedId));

        // when
        final var actualResponse = this.mvc.perform(actualRequest);

        // then
        actualResponse
            .andExpect(status().isNotFound())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(this.getCustomerUseCase).execute(expectedId);
    }
}