package com.example.tfgoposchool.dtos.profesores;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfesorDto {
    Long id;
    String nombre;
    String apellidos;
    String email;
    String telefono;
    String dni;
    String titulosAcademicos;
    Double salarioMensual;
    List<String> grupos;
}
