package io.github.gabrielmsouza.library.infrastructure.customer.persistence;

import io.github.gabrielmsouza.library.domain.customer.Customer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "Customers")
@Table(name = "customers")
public class CustomerJpaEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Deprecated
    CustomerJpaEntity() {}

    private CustomerJpaEntity(final String id) {
        this.id = id;
    }

    private CustomerJpaEntity(final String id, final String name, final String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static CustomerJpaEntity from(final String id) {
        return new CustomerJpaEntity(id);
    }

    public static CustomerJpaEntity from(final Customer customer) {
        return new CustomerJpaEntity(customer.getId(), customer.getName(), customer.getEmail());
    }

    public Customer toCustomer() {
        return Customer.with(this.id, this.name, this.email);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
