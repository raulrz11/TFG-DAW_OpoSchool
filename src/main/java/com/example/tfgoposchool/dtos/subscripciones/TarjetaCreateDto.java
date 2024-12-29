package com.example.tfgoposchool.dtos.subscripciones;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TarjetaCreateDto {
    @NotBlank(message = "El numero de la tarjeta no puede estar vacio")
    @Pattern(regexp = "\\d{16}", message = "El numero de la tarjeta debe tener 16 digitos")
    String numTarjeta;
    @NotBlank(message = "La fecha de expiracion no puede estar vacia")
    @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{2}", message = "Formato invalido: Ej: MM/YY")
    String fechaExpiracion;
    @NotBlank(message = "El cvv no puede estar vacio")
    @Pattern(regexp = "\\d{3}", message = "El cvv debe tener 3 digitos")
    String codigoSeguridad;
}
