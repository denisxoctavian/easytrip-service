package com.easytrip.backend.exception;

import org.springframework.http.HttpStatus;

public class AppException extends  RuntimeException{

    private final HttpStatus httpStatus;

    public AppException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }


}
