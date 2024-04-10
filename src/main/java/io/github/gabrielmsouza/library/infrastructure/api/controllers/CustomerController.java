package io.github.gabrielmsouza.library.infrastructure.api.controllers;

import io.github.gabrielmsouza.library.application.customer.CreateCustomerUseCase;
import io.github.gabrielmsouza.library.application.customer.GetCustomerByIdUseCase;
import io.github.gabrielmsouza.library.infrastructure.api.CustomerAPI;
import io.github.gabrielmsouza.library.infrastructure.customer.models.CreateCustomerRequest;
import io.github.gabrielmsouza.library.infrastructure.customer.models.CustomerResponse;
import io.github.gabrielmsouza.library.infrastructure.customer.presenter.CustomerPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CustomerController implements CustomerAPI {
    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerByIdUseCase getCustomerByIdUseCase;

    public CustomerController(
            final CreateCustomerUseCase createCustomerUseCase,
            final GetCustomerByIdUseCase getCustomerByIdUseCase
    ) {
        this.createCustomerUseCase = Objects.requireNonNull(createCustomerUseCase);
        this.getCustomerByIdUseCase = Objects.requireNonNull(getCustomerByIdUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateCustomerRequest request) {
        final var output = this.createCustomerUseCase.execute(CreateCustomerUseCase.Input.from(request));
        return ResponseEntity.created(URI.create("/customers/" + output.id())).body(output);
    }

    @Override
    public ResponseEntity<CustomerResponse> getById(final String id) {
        return ResponseEntity.ok(CustomerPresenter.present(this.getCustomerByIdUseCase.execute(id)));
    }
}
