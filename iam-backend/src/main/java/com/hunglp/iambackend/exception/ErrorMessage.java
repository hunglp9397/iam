package com.hunglp.iambackend.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorMessage {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;

    public static ErrorMessage getMessage(Exception ex, WebRequest request, HttpStatus httpStatus){
        return new ErrorMessage(
               httpStatus.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }



}
