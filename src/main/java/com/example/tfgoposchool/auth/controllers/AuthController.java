package com.example.tfgoposchool.auth.controllers;

import com.example.tfgoposchool.auth.dto.JwtAuthDto;
import com.example.tfgoposchool.auth.dto.SignIn;
import com.example.tfgoposchool.auth.services.auth.AuthServiceImpl;
import com.example.tfgoposchool.dtos.alumnos.AlumnoCreateDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthServiceImpl service;

    public AuthController(AuthServiceImpl service) {
        this.service = service;
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthDto> signIn(@Validated @RequestBody SignIn request){
        return ResponseEntity.ok(service.signIn(request));
    }

    @PostMapping("/signup")
    public ResponseEntity<List<String>> signUp(@Validated @RequestBody AlumnoCreateDto request) {
        return ResponseEntity.ok(service.signUp(request));
    }

    @PostMapping("/validateForm")
    public ResponseEntity<Boolean> validateRegisterForm(@Validated @RequestBody AlumnoCreateDto request) {
        return ResponseEntity.ok(service.validateRegisterForm(request));
    }

    //metodo que permite mostrar los mensajes de error
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
