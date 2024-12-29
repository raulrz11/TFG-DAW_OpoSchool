package com.example.tfgoposchool.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignIn {
    @NotBlank(message = "El nombre de usuario no puede estar vacio")
    String username;
    @NotBlank(message = "La contraseña no puede estar vacia")
    String password;
}
