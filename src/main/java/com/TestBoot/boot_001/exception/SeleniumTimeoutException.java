package com.TestBoot.boot_001.exception;

public class SeleniumTimeoutException extends RuntimeException {

    public SeleniumTimeoutException(String message) {
        super(message);
    }

    public SeleniumTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}