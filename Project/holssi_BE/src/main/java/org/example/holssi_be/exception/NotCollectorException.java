package org.example.holssi_be.exception;

public class NotCollectorException extends RuntimeException {
    public NotCollectorException() {
        super("Member is not a Collector");
    }
}