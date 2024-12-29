package com.example.tfgoposchool.dtos.grupos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GrupoDto {
    Long id;
    String nombre;
    String fechaClase;
    String horaClase;
    String duracionClase;
    String profesor;
    List<String> alumnos;
    List<String> tareas;
}
