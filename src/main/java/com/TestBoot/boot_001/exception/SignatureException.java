package com.TestBoot.boot_001.exception;

public class SignatureException extends PreSaleException {
    public SignatureException(String message) {
        super(message);
    }

    public SignatureException(String message, Throwable cause) {
        super(message, cause);
    }
}
