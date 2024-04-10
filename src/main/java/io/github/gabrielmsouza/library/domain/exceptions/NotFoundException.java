package io.github.gabrielmsouza.library.domain.exceptions;

import io.github.gabrielmsouza.library.domain.validation.Error;

import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException {
    protected NotFoundException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    public static NotFoundException with(final Class<?> clazz, final String id) {
        final var anError = "%s with ID %s was not found".formatted(clazz.getSimpleName(), id);
        return new NotFoundException(anError, Collections.emptyList());
    }

    public static NotFoundException with(final String message) {
        return new NotFoundException(message, Collections.emptyList());
    }

    public static NotFoundException with(final Error anError) {
        return new NotFoundException(anError.message(), List.of(anError));
    }
}
