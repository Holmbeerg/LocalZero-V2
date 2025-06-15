package com.localzero.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.net.URI;

/**
 * Global exception handler
 * Handles various exceptions and returns appropriate HTTP responses with problem details.
 * Adheres to RFC 7807 for problem details in HTTP APIs.
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InitiativeNotFoundException.class)
    public ProblemDetail handleInitiativeNotFoundException(InitiativeNotFoundException ex) {
        log.error("Initiative not found: {}", ex.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Initiative Not Found");
        problemDetail.setType(URI.create("/problems/initiative-not-found"));
        return problemDetail;
    }

    @ExceptionHandler(EcoActionNotFoundException.class)
    public ProblemDetail handleEcoActionNotFoundException(EcoActionNotFoundException ex) {
        log.error("EcoAction not found: {}", ex.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Eco Action Not Found");
        problemDetail.setType(URI.create("/problems/eco-action-not-found"));
        return problemDetail;
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ProblemDetail handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        log.error("Email already exists: {}", ex.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Email Already Exists");
        problemDetail.setType(URI.create("/problems/email-already-exists"));
        return problemDetail;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFoundException(UserNotFoundException ex) {
        log.error("User not found: {}", ex.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("User Not Found");
        problemDetail.setType(URI.create("/problems/user-not-found"));
        return problemDetail;
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ProblemDetail handleRoleNotFoundException(RoleNotFoundException ex) {
        log.error("Role not found: {}", ex.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Role Not Found");
        problemDetail.setType(URI.create("/problems/role-not-found"));
        return problemDetail;
    }
}