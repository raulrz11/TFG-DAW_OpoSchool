package com.example.tfgoposchool.dtos.tareas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TareaDto {
    Long id;
    String titulo;
    String descripcion;
    String fechaEntrega;
    String archivoUrl;
    List<String> entregas;
}
