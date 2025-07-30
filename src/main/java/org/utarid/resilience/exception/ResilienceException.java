package org.utarid.resilience.exception;

/**
 * A dedicated exception for resilience-related errors.
 * This exception is used instead of generic RuntimeException to provide more specific error handling.
 */
public class ResilienceException extends RuntimeException {

    public ResilienceException(String message) {
        super(message);
    }

    public ResilienceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResilienceException(Throwable cause) {
        super(cause);
    }
}