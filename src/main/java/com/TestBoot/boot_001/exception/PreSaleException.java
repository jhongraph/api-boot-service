package com.TestBoot.boot_001.exception;

public class PreSaleException extends RuntimeException {
    public PreSaleException(String message) {
        super(message);
    }

    public PreSaleException(String message, Throwable cause) {
        super(message, cause);
    }
}
