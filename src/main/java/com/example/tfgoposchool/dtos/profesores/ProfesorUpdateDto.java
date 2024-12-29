package com.example.tfgoposchool.dtos.profesores;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfesorUpdateDto {
    @Size(min = 3, max = 30, message = "El nombre debe tener entre 3 y 30 caracteres")
    String nombre;
    @Size(min = 3, max = 30, message = "El nombre debe tener entre 3 y 30 caracteres")
    String apellidos;
    @Email(message = "Debes proporcionar un email valido. Ej: xxx@xxx.com")
    String email;
    @Pattern(regexp = "^[679]\\d{8}$", message = "El teléfono debe comenzar con 6, 7 o 9 y tener exactamente 9 dígitos")
    String telefono;
    @Pattern(regexp = "\\d{8}[A-Za-z]", message = "El DNI debe tener 8 números seguidos de una letra")
    String dni;
    String titulosAcademicos;
}
