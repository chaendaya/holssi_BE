package org.example.holssi_be.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException() {
        super("Member not found");
    }
}