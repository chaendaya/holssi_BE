package org.example.holssi_be.exception;

public class NotUserException extends RuntimeException {
    public NotUserException(String message) {
        super(message);
    }
}