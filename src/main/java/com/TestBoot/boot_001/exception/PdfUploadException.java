package com.TestBoot.boot_001.exception;

public class PdfUploadException extends PreSaleException {
    public PdfUploadException(String message) {
        super(message);
    }

    public PdfUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}

