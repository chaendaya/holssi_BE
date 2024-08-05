package org.example.holssi_be.exception;

public class NotAdminException extends RuntimeException {
    public NotAdminException() {
        super("Member is not an Admin");
    }
}
