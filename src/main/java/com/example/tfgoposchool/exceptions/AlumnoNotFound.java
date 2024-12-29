package com.example.tfgoposchool.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AlumnoNotFound extends RuntimeException{
    public AlumnoNotFound(String message) {
        super(message);
    }
}
