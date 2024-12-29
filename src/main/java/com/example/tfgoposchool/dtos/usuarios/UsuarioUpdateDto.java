package com.example.tfgoposchool.dtos.usuarios;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioUpdateDto {
    @Email(message = "Debes proporcionar un email valido. Ej: xxx@xxx.com")
    String email;
    String password;
}
