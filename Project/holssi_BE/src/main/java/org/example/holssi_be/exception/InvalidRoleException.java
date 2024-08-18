package org.example.holssi_be.exception;

public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException() {
        super("Invalid role specified.");
    }
}
