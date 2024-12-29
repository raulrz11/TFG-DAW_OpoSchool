package com.example.tfgoposchool.exceptions.entregas;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntregaBadRequest extends RuntimeException{
    public EntregaBadRequest(String message) {
        super(message);
    }
}
