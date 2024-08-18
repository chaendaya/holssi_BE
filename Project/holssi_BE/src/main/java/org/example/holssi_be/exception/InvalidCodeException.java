package org.example.holssi_be.exception;

public class InvalidCodeException extends RuntimeException {
    public InvalidCodeException() { super("Verification Code does not match."); }
}
