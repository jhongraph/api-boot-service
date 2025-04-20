package com.TestBoot.boot_001.exception;

// Excepción específica para errores al llenar formularios / Specific exception for form input errors
public class FormException extends PreSaleException {
    public FormException(String message) {
        super(message);
    }
}
