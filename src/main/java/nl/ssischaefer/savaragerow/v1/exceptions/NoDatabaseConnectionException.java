package nl.ssischaefer.savaragerow.v1.exceptions;

public class NoDatabaseConnectionException extends Exception {
    public NoDatabaseConnectionException(String message) {
        super(message);
    }
}
