package com.hunglp.iambackend.exception;


import org.keycloak.authorization.client.util.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ErrorMessage.getMessage(ex,
                request, HttpStatus.valueOf(HttpStatus.NOT_FOUND.value())), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ErrorMessage.getMessage(ex,
                request, HttpStatus.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorMessage> unauthorizedException(UnauthorizedException ex, WebRequest request) {

        return new ResponseEntity<>(ErrorMessage.getMessage(ex,
                request, HttpStatus.valueOf(HttpStatus.UNAUTHORIZED.value())), HttpStatus.UNAUTHORIZED);
    }


}
