package com.belov.mycloud.controller;

import com.belov.mycloud.domain.Error;
import com.belov.mycloud.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomFileException(CustomException ce) {
        //todo - придумать счетчик id операции. id операции должен попадать в логи.
        if (ce.getHttpStatus() == HttpStatus.INTERNAL_SERVER_ERROR) {
            System.out.println(ce.getMessage());
            ce.getCause().printStackTrace();
        }
        return new ResponseEntity<Object>(new Error(ce.getMessage(), ce.getId()), ce.getHttpStatus());
    }
}