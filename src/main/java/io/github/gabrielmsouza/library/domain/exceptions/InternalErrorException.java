package io.github.gabrielmsouza.library.domain.exceptions;

public class InternalErrorException extends NoStacktraceException {

    protected InternalErrorException(final String aMessage, final Throwable aCause) {
        super(aMessage, aCause);
    }

    public static InternalErrorException with(final String message, final Throwable cause) {
        return new InternalErrorException(message, cause);
    }

    public static InternalErrorException with(final String message) {
        return InternalErrorException.with(message, null);
    }
}
