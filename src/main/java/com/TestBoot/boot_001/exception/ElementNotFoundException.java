package com.TestBoot.boot_001.exception;

public class ElementNotFoundException extends PreSaleException {
    public ElementNotFoundException(String message) {
        super(message);
    }

    public ElementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
