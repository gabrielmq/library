package io.github.gabrielmsouza.library.infrastructure.configuration.usecases;

import io.github.gabrielmsouza.library.application.customer.CreateCustomerUseCase;
import io.github.gabrielmsouza.library.application.customer.GetCustomerByIdUseCase;
import io.github.gabrielmsouza.library.domain.customer.CustomerGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration(proxyBeanMethods = false)
public class CustomerUseCaseConfiguration {
    private final CustomerGateway gateway;

    public CustomerUseCaseConfiguration(final CustomerGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Bean
    CreateCustomerUseCase createCustomerUseCase() {
        return new CreateCustomerUseCase(this.gateway);
    }

    @Bean
    GetCustomerByIdUseCase getCustomerByIdUseCase() {
        return new GetCustomerByIdUseCase(this.gateway);
    }
}
