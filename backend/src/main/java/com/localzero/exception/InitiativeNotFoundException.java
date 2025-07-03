package com.localzero.exception;


public class InitiativeNotFoundException extends RuntimeException {
    public InitiativeNotFoundException(long id) {
        super("Initiative not found with ID: " + id);
    }
}
