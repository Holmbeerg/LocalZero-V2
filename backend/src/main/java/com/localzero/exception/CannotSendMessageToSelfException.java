package com.localzero.exception;

public class CannotSendMessageToSelfException extends RuntimeException {
    public CannotSendMessageToSelfException(String message) {
        super(message);
    }
}
