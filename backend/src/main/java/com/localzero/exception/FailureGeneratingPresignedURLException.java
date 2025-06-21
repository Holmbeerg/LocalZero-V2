package com.localzero.exception;

public class FailureGeneratingPresignedURLException extends RuntimeException {
    public FailureGeneratingPresignedURLException(String message, Throwable cause) {
        super(message, cause);
    }
}
