package io.aero.exceptions;

public class NoDatabaseConnectionException extends Exception {
    public NoDatabaseConnectionException(String message) {
        super(message);
    }
}
