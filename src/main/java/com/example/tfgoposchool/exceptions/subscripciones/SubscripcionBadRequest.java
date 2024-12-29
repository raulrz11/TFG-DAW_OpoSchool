package com.example.tfgoposchool.exceptions.subscripciones;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SubscripcionBadRequest extends RuntimeException{
    public SubscripcionBadRequest(String message) {
        super(message);
    }
}
