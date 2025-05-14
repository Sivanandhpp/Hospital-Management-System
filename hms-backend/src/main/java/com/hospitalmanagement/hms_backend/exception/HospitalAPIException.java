package com.hospitalmanagement.hms_backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HospitalAPIException extends RuntimeException {
    private HttpStatus status;
    private String message;

    public HospitalAPIException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public HospitalAPIException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }
}