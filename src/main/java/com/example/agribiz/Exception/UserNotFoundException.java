package com.example.agribiz.Exception;

// User Not Found Exception
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

