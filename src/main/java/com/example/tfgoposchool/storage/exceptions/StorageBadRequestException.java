package com.example.tfgoposchool.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StorageBadRequestException extends RuntimeException{
    public StorageBadRequestException(String message) {
        super(message);
    }
}
