package io.github.gabrielmsouza.library.application.customer;

import io.github.gabrielmsouza.library.application.UseCase;
import io.github.gabrielmsouza.library.domain.customer.Customer;
import io.github.gabrielmsouza.library.domain.customer.CustomerGateway;
import io.github.gabrielmsouza.library.domain.exceptions.NotificationException;
import io.github.gabrielmsouza.library.domain.validation.handler.Notification;
import io.github.gabrielmsouza.library.infrastructure.customer.models.CreateCustomerRequest;

public class CreateCustomerUseCase implements UseCase<CreateCustomerUseCase.Input, CreateCustomerUseCase.Output> {
    private final CustomerGateway gateway;

    public CreateCustomerUseCase(final CustomerGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Output execute(final Input input) {
        final var notification = Notification.create();
        final var customer = notification.validate(() -> Customer.with(input.name(), input.email()));
        if (notification.hasErrors()) {
            throw NotificationException.with("Invalid customer", notification);
        }
        return Output.from(this.gateway.create(customer).getId());
    }

    public record Input(String name, String email) {
        public static Input from(final CreateCustomerRequest request) {
            return new Input(request.name(), request.email());
        }
    }

    public record Output(String id) {
        public static Output from(final String id) {
            return new Output(id);
        }
    }
}
