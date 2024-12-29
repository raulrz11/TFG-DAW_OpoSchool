package com.example.tfgoposchool.dtos.subscripciones;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscripcionDto {
    Long id;
    Long alumnoId;
    String alumno;
    String fechaInicio;
    String fechaFin;
    String estado;
    Double precio;
    String createdAt;
    String updatedAt;
    String deletedAt;
}
