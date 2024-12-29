package com.example.tfgoposchool.dtos.alumnos;

import com.example.tfgoposchool.dtos.subscripciones.TarjetaCreateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlumnoCreateDto {
    @NotBlank(message = "El nombre no puede estar vacio")
    @Size(min = 3, max = 30, message = "El nombre debe tener entre 3 y 30 caracteres")
    String nombre;
    @NotBlank(message = "El nombre no puede estar vacio")
    @Size(min = 3, max = 30, message = "El nombre debe tener entre 3 y 30 caracteres")
    String apellidos;
    @NotBlank(message = "El email no puede estar vacio")
    @Email(message = "Debes proporcionar un email valido. Ej: xxx@xxx.com")
    String email;
    @NotBlank(message = "El telefono no puede estar vacio")
    @Pattern(regexp = "^[679]\\d{8}$", message = "El teléfono debe comenzar con 6, 7 o 9 y tener 9 dígitos")
    String telefono;
    @NotBlank(message = "El DNI no puede estar vacio")
    @Pattern(regexp = "\\d{8}[A-Za-z]", message = "El DNI debe tener 8 números seguidos de una letra")
    String dni;
    @Valid
    TarjetaCreateDto tarjeta;
}
