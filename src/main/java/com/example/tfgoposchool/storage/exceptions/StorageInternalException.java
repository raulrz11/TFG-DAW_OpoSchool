package com.example.tfgoposchool.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class StorageInternalException extends RuntimeException{
    public StorageInternalException(String message) {
        super(message);
    }
}
