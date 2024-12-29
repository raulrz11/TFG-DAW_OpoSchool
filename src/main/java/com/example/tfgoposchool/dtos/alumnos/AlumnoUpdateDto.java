package com.example.tfgoposchool.dtos.alumnos;

import com.example.tfgoposchool.models.Grupo;
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
public class AlumnoUpdateDto {
    @Size(min = 3, max = 30, message = "El nombre debe tener entre 3 y 30 caracteres")
    String nombre;
    @Size(min = 3, max = 30, message = "El nombre debe tener entre 3 y 30 caracteres")
    String apellidos;
    @Pattern(regexp = "^[679]\\d{8}$", message = "El teléfono debe comenzar con 6, 7 o 9 y tener exactamente 9 dígitos")
    String telefono;
    @Pattern(regexp = "\\d{8}[A-Za-z]", message = "El DNI debe tener 8 números seguidos de una letra")
    String dni;
}
