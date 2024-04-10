package io.github.gabrielmsouza.library.infrastructure.api.controllers;

import io.github.gabrielmsouza.library.domain.exceptions.DomainException;
import io.github.gabrielmsouza.library.domain.exceptions.InternalErrorException;
import io.github.gabrielmsouza.library.domain.exceptions.NotFoundException;
import io.github.gabrielmsouza.library.domain.validation.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<?> handleDomainException(final DomainException ex) {
        return ResponseEntity.unprocessableEntity().body(ApiError.from(ex));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(final NotFoundException ex) {
        return ResponseEntity.status(NOT_FOUND).body(ApiError.from(ex));
    }

    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<?> handleInternalErrorException(final InternalErrorException ex) {
        return ResponseEntity.internalServerError().body(ApiError.from(ex));
    }

    public record ApiError(String message, List<Error> errors) {
        public static ApiError from(final DomainException ex) {
            return new ApiError(ex.getMessage(), ex.getErrors());
        }

        public static ApiError from(final InternalErrorException ex) {
            return new ApiError(ex.getMessage(), null);
        }
    }
}
