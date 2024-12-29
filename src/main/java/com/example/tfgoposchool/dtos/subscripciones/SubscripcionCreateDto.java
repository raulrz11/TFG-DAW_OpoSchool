package com.example.tfgoposchool.dtos.subscripciones;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscripcionCreateDto {
    @NotNull(message = "La tarjeta de credito no puede estar vacia")
    @Valid
    TarjetaCreateDto tarjeta;
}
