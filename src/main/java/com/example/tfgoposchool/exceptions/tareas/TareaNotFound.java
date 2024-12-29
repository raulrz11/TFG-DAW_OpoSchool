package com.example.tfgoposchool.exceptions.tareas;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TareaNotFound extends RuntimeException{
    public TareaNotFound(String message) {
        super(message);
    }
}
