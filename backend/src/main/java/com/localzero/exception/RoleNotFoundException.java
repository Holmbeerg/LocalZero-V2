package com.localzero.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super("Role not found in database: " + message);
    }
}
