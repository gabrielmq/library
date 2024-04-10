package io.github.gabrielmsouza.library.infrastructure.customer;

import io.github.gabrielmsouza.library.Fixture;
import io.github.gabrielmsouza.library.domain.customer.Customer;
import io.github.gabrielmsouza.library.infrastructure.H2GatewayTest;
import io.github.gabrielmsouza.library.infrastructure.customer.persistence.CustomerJpaEntity;
import io.github.gabrielmsouza.library.infrastructure.customer.persistence.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@H2GatewayTest
class CustomerH2GatewayTest {
    @Autowired
    private CustomerH2Gateway gateway;

    @Autowired
    private CustomerRepository repository;

    @Test
    void givenAValidCustomer_whenCallsCreate_thenShouldPersistIt() {
        // given
        final var expectedName = Fixture.Customer.name();
        final var expectedEmail = Fixture.Customer.email();

        final var customer = Customer.with(expectedName, expectedEmail);
        final var expectedId = customer.getId();

        assertEquals(0, this.repository.count());

        // when
        final var actualCustomer = this.gateway.create(Customer.from(customer));

        // then
        assertEquals(1, this.repository.count());
        assertEquals(expectedId, actualCustomer.getId());
        assertEquals(expectedName, actualCustomer.getName());
        assertEquals(expectedEmail, actualCustomer.getEmail());

        final var persistedCustomer = this.repository.findById(expectedId).get();
        assertEquals(expectedId, persistedCustomer.getId());
        assertEquals(expectedName, persistedCustomer.getName());
        assertEquals(expectedEmail, persistedCustomer.getEmail());
    }

    @Test
    void givenAValidId_whenCallsFindById_thenShouldReturnIt() {
        // given
        final var expectedName = Fixture.Customer.name();
        final var expectedEmail = Fixture.Customer.email();

        final var customer = Customer.with(expectedName, expectedEmail);
        final var expectedId = customer.getId();

        this.repository.saveAndFlush(CustomerJpaEntity.from(customer));
        assertEquals(1, this.repository.count());

        // when
        final var actualCustomer = this.gateway.findById(expectedId).get();

        // then
        assertEquals(expectedId, actualCustomer.getId());
        assertEquals(expectedName, actualCustomer.getName());
        assertEquals(expectedEmail, actualCustomer.getEmail());
    }

    @Test
    void givenAnInvalidId_whenCallsFindById_thenShouldReturnEmpty() {
        // given
        final var expectedName = Fixture.Customer.name();
        final var expectedEmail = Fixture.Customer.email();

        final var customer = Customer.with(expectedName, expectedEmail);

        this.repository.saveAndFlush(CustomerJpaEntity.from(customer));
        assertEquals(1, this.repository.count());

        // when
        final var actualCustomer = this.gateway.findById("123");

        // then
        assertTrue(actualCustomer.isEmpty());
    }
}