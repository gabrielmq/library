package io.github.gabrielmsouza.library.infrastructure.customer.presenter;

import io.github.gabrielmsouza.library.application.customer.GetCustomerByIdUseCase;
import io.github.gabrielmsouza.library.infrastructure.customer.models.CustomerResponse;

public final class CustomerPresenter {
    private CustomerPresenter() {
    }

    public static CustomerResponse present(final GetCustomerByIdUseCase.Output output) {
        return new CustomerResponse(
            output.id(),
            output.name(),
            output.email()
        );
    }
}
