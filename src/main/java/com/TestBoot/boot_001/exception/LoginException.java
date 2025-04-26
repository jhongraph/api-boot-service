package com.TestBoot.boot_001.exception;

import javax.annotation.Nullable;

// Excepción específica para errores de login / Specific exception for login-related issues
public class LoginException extends PreSaleException {
    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }


}
