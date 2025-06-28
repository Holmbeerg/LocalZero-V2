package com.localzero.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(long id) {
        super("Post not found with ID: " + id);
    }
}
