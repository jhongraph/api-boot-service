package com.TestBoot.boot_001.exception;

public class FormException extends PreSaleException {
    public FormException(String message) {
        super(message);
    }
    public FormException(String message, Throwable cause) {
        super(message, cause);
    }
}
