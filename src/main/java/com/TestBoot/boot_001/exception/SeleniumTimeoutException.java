package com.TestBoot.boot_001.exception;

public class SeleniumTimeoutException extends RuntimeException {

    // Constructor con mensaje
    public SeleniumTimeoutException(String message) {
        super(message);
    }

    // Constructor con mensaje y causa (útil para wrappear excepciones originales)
    public SeleniumTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}