package io.github.gabrielmsouza.library.infrastructure.customer.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerJpaEntity, String> {
}
