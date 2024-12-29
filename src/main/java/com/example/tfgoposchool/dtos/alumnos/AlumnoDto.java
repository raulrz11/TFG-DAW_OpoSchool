package com.example.tfgoposchool.dtos.alumnos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlumnoDto {
    Long id;
    String nombre;
    String apellidos;
    String email;
    String telefono;
    String dni;
    String grupo;
    String usuario;
    String subscripcion;
}
