package com.example.tfgoposchool.exceptions.grupos;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GrupoNotFound extends RuntimeException{
    public GrupoNotFound(String message) {
        super(message);
    }
}
