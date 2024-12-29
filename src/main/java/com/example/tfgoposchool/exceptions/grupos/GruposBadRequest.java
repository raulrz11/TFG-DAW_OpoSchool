package com.example.tfgoposchool.exceptions.grupos;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GruposBadRequest extends RuntimeException{
    public GruposBadRequest(String message) {
        super(message);
    }
}
