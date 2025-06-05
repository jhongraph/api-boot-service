package com.TestBoot.boot_001.exception;

public class LoginException extends PreSaleException {
    public LoginException(String message) {
        super(message);
    }
    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }


}
