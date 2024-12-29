package com.example.tfgoposchool.dtos.entregas;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntregaUpdateDto {
    @NotNull(message = "La calificacion no puede estar vacia")
    @Min(value = 0, message = "La calificación no puede ser menor a 0")
    @Max(value = 10, message = "La calificación no puede ser mayor a 10")
    private Double calificacion;
    private String comentarios;
}
