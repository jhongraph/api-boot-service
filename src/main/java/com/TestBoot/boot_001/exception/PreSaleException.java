package com.TestBoot.boot_001.exception;

// Superclass for all custom exceptions
public class PreSaleException extends RuntimeException {
    public PreSaleException(String message) {
        super(message);
    }

    public PreSaleException(String message, Throwable cause) {
        super(message, cause);
    }
}
