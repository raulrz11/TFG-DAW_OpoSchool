package com.example.tfgoposchool.dtos.entregas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntregaDto {
    Long id;
    String estado;
    String fechaEntrega;
    String archivoUrl;
    Double calificacion;
    String comentarios;
    String tarea;
    String alumno;
}
