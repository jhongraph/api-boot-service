package com.TestBoot.boot_001.exception;

// Excepción para elementos no encontrados o esperas fallidas / Exception for not found elements or wait errors
public class ElementNotFoundException extends PreSaleException {
    public ElementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
