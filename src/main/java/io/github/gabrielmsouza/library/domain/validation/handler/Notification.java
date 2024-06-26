package io.github.gabrielmsouza.library.domain.validation.handler;

import io.github.gabrielmsouza.library.domain.exceptions.DomainException;
import io.github.gabrielmsouza.library.domain.validation.Error;
import io.github.gabrielmsouza.library.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {
    private final List<Error> errors;

    private Notification(final List<Error> errors) {
        this.errors = errors;
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Error anError) {
        return create().append(anError);
    }

    public static Notification create(final Throwable anError) {
        return create(Error.with(anError.getMessage()));
    }

    @Override
    public Notification append(final Error anError) {
        errors.add(anError);
        return this;
    }

    @Override
    public Notification append(final ValidationHandler anHandler) {
        this.errors.addAll(anHandler.getErrors());
        return this;
    }

    @Override
    public <T> T validate(final Validation<T> aValidation) {
        try {
            return aValidation.validate();
        } catch (final DomainException ex) {
            this.errors.addAll(ex.getErrors());
        } catch (final Throwable t) {
            this.errors.add(Error.with(t.getMessage()));
        }
        return null;
    }

    @Override
    public List<Error> getErrors() {
        return errors;
    }
}
