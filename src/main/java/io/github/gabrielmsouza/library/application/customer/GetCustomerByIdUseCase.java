package io.github.gabrielmsouza.library.application.customer;

import io.github.gabrielmsouza.library.application.UseCase;
import io.github.gabrielmsouza.library.domain.customer.Customer;
import io.github.gabrielmsouza.library.domain.customer.CustomerGateway;
import io.github.gabrielmsouza.library.domain.exceptions.NotFoundException;

public class GetCustomerByIdUseCase implements UseCase<String, GetCustomerByIdUseCase.Output> {
    private final CustomerGateway gateway;

    public GetCustomerByIdUseCase(final CustomerGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Output execute(final String id) {
        return this.gateway.findById(id)
                .map(Output::from)
                .orElseThrow(() -> NotFoundException.with(Customer.class ,id));
    }

    public record Output(String id, String name, String email) {
        public static Output from(final Customer customer) {
            return new Output(customer.getId(), customer.getName(), customer.getEmail());
        }
    }
}
