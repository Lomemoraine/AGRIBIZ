package com.example.agribiz.Exception;

// Invalid Token Exception
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
