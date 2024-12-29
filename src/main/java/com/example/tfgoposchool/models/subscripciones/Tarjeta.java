package com.example.tfgoposchool.models.subscripciones;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class Tarjeta {
    private String numTarjeta;

    private String fechaExpiracion;

    private String codigoSeguridad;
}
