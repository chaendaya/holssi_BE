package org.example.holssi_be.exception;

public class NotUserException extends RuntimeException {
    public NotUserException() {
        super("Member is not a User");
    }
}