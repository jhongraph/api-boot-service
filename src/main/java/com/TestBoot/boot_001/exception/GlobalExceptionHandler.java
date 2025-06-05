package com.TestBoot.boot_001.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Object> buildResponse(String message, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<Object> handleLoginException(LoginException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<Object> handleElementNotFound(ElementNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PdfUploadException.class)
    public ResponseEntity<Object> handlePdfUpload(PdfUploadException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FormException.class)
    public ResponseEntity<Object> handleForm(FormException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Object> handleSignature(SignatureException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SeleniumTimeoutException.class)
    public ResponseEntity<Object> handleTimeout(SeleniumTimeoutException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArg(IllegalArgumentException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PreSaleException.class)
    public ResponseEntity<Object> handleGenericPreSale(PreSaleException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex) {
        return buildResponse("Unexpected error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
