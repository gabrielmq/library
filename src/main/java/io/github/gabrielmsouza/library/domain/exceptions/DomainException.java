package io.github.gabrielmsouza.library.domain.exceptions;

import io.github.gabrielmsouza.library.domain.validation.Error;

import java.util.ArrayList;
import java.util.List;

public class DomainException extends NoStacktraceException {
    protected final List<Error> errors;

    protected DomainException(final String aMessage, final List<Error> anErrors) {
        super(aMessage);
        this.errors = anErrors;
    }

    public static DomainException with(final Error anError) {
        return new DomainException(anError.message(), List.of(anError));
    }

    public static DomainException with(final String message) {
        return new DomainException(message, new ArrayList<>());
    }

    public static DomainException with(final List<Error> anErrors) {
        return new DomainException("", anErrors);
    }

    public List<Error> getErrors() {
        return errors;
    }
}
