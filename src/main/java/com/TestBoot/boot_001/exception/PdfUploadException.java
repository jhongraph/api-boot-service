package com.TestBoot.boot_001.exception;

// Excepción específica para errores en carga de PDF / Specific exception for PDF upload errors
public class PdfUploadException extends PreSaleException {
    public PdfUploadException(String message) {
        super(message);
    }
}

