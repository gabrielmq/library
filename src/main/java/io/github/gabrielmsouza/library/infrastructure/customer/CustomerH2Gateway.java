package io.github.gabrielmsouza.library.infrastructure.customer;

import io.github.gabrielmsouza.library.domain.customer.Customer;
import io.github.gabrielmsouza.library.domain.customer.CustomerGateway;
import io.github.gabrielmsouza.library.infrastructure.customer.persistence.CustomerJpaEntity;
import io.github.gabrielmsouza.library.infrastructure.customer.persistence.CustomerRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class CustomerH2Gateway implements CustomerGateway {
    private final CustomerRepository repository;

    public CustomerH2Gateway(final CustomerRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Customer create(final Customer customer) {
        return this.repository.save(CustomerJpaEntity.from(customer)).toCustomer();
    }

    @Override
    public Optional<Customer> findById(final String id) {
        return this.repository.findById(id).map(CustomerJpaEntity::toCustomer);
    }
}
