package com.TestBoot.boot_001.exception;

public class LoteRegisterException extends RuntimeException {
    public LoteRegisterException(String message) {
        super(message);
    }

    public LoteRegisterException(String message, Throwable cause) {
        super(message, cause);
    }
}
