package io.github.gabrielmsouza.library.domain.customer;

import io.github.gabrielmsouza.library.domain.exceptions.NotificationException;
import io.github.gabrielmsouza.library.domain.validation.Error;
import io.github.gabrielmsouza.library.domain.validation.ValidationHandler;
import io.github.gabrielmsouza.library.domain.validation.handler.Notification;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class Customer {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(.+)@(.+)$");
    private String id;
    private String name;
    private String email;

    private Customer(final String id, final String name, final String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        selfValidation();
    }

    public static Customer with(final String name, final String email) {
        final var id = UUID.randomUUID().toString().toLowerCase().replace("-", "");
        return new Customer(id, name, email);
    }

    public static Customer with(final String id, final String name, final String email) {
        return new Customer(id, name, email);
    }

    public static Customer from(final Customer customer) {
        return new Customer(customer.id, customer.name, customer.email);
    }

    public void validate(final ValidationHandler handler) {
        if (Objects.isNull(this.id) || this.id.isBlank()) {
            handler.append(Error.with("'id' should not be empty"));
        }
        if (Objects.isNull(this.name) || this.name.isBlank()) {
            handler.append(Error.with("'name' should not be empty"));
        }
        if (Objects.isNull(this.email) || this.email.isBlank() || !EMAIL_PATTERN.matcher(this.email).matches()) {
            handler.append(Error.with("'email' should have a valid format"));
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    private void selfValidation() {
        final var notification = Notification.create();
        validate(notification);
        if (notification.hasErrors()) {
            throw NotificationException.with("Failed to create a Customer", notification);
        }
    }
}
