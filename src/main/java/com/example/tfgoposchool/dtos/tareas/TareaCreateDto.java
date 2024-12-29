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
public class TareaCreateDto {
    @NotBlank(message = "El titulo no puede estar vacio")
    @Size(min = 3, max = 30, message = "El titulo debe tener entre 3 y 30 caracteres")
    String titulo;
    @NotBlank(message = "El nombre no puede estar vacio")
    String descripcion;
    @NotNull(message = "La fecha de la entrega no puede estar vacia")
    @FutureOrPresent(message = "La fecha de la entrega debe ser igual o superior a la actual")
    LocalDate fechaEntrega;
}
