package com.hunglp.iambackend.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class IAMResponse<T> extends ResponseEntity<T> {
    public IAMResponse(HttpStatus status) {
        super(status);
    }

    public IAMResponse(T body, HttpStatus status) {
        super(body, status);
    }

    public IAMResponse(MultiValueMap<String, String> headers, HttpStatus status) {
        super(headers, status);
    }

    public IAMResponse(T body, MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers, status);
    }

    public IAMResponse(T body, MultiValueMap<String, String> headers, int rawStatus) {
        super(body, headers, rawStatus);
    }


}
