package com.localzero.exception;


public class InitiativeNotFoundException extends RuntimeException {
    public InitiativeNotFoundException(Long id) {
        super("Initiative not found with ID: " + id);
    }
}
