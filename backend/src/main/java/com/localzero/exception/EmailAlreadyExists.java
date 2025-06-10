package com.localzero.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailAlreadyExists extends RuntimeException {
    public EmailAlreadyExists(String email) {
        super("Email already exists: " + email);
    }
}
