package com.example.agribiz.Exception;

public class UnverifiedUserException extends RuntimeException {
    public UnverifiedUserException(String message) {
        super(message);
    }
}