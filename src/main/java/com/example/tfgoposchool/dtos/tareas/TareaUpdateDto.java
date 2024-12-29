package com.example.tfgoposchool.dtos.tareas;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TareaUpdateDto {
    @Size(min = 3, max = 30, message = "El titulo debe tener entre 3 y 30 caracteres")
    String titulo;
    String descripcion;
    @FutureOrPresent(message = "La fecha de la entrega debe ser igual o superior a la actual")
    LocalDate fechaEntrega;
}
