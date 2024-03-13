package com.belov.mycloud.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomException extends Exception{
    private final Integer id;
    private final HttpStatus httpStatus;

    public CustomException(String message, Integer id, HttpStatus httpStatus) {
        super(message);
        this.id = id;
        this.httpStatus = httpStatus;
    }

    public CustomException(String message, Integer id, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.id = id;
        this.httpStatus = httpStatus;
    }
}