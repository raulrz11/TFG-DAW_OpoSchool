package com.example.tfgoposchool.exceptions.subscripciones;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubscripcionNotFound extends RuntimeException{
    public SubscripcionNotFound(String message) {
        super(message);
    }
}
