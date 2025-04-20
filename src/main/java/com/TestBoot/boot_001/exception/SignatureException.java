package com.TestBoot.boot_001.exception;

// Excepción específica para errores de firma / Specific exception for signature errors
public class SignatureException extends PreSaleException {
    public SignatureException(String message) {
        super(message);
    }
}
