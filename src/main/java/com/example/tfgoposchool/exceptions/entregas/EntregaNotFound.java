package com.example.tfgoposchool.exceptions.entregas;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntregaNotFound extends RuntimeException{
    public EntregaNotFound(String message) {
        super(message);
    }
}
