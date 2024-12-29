package com.example.tfgoposchool.auth.services.auth;

import com.example.tfgoposchool.auth.dto.JwtAuthDto;
import com.example.tfgoposchool.auth.dto.SignIn;
import com.example.tfgoposchool.dtos.alumnos.AlumnoCreateDto;

import java.util.List;

public interface AuthService {
    JwtAuthDto signIn(SignIn signIn);

    List<String> signUp(AlumnoCreateDto signUp);
}
