package io.github.gabrielmsouza.library.domain.customer;

import java.util.Optional;

public interface CustomerGateway {
    Customer create(Customer customer);
    Optional<Customer> findById(String id);
}
