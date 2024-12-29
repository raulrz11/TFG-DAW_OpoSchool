package com.example.tfgoposchool.dtos.grupos;

import com.example.tfgoposchool.models.Alumno;
import com.example.tfgoposchool.models.Profesor;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GrupoCreateDto {
    @NotBlank(message = "El nombre no puede estar vacio")
    @Size(min = 1, max = 30, message = "El nombre debe tener entre 1 y 30 caracteres")
    String nombre;
    @NotNull(message = "La fecha de la clase no puede estar vacia")
    @FutureOrPresent(message = "La fecha de la clase debe ser igual o superior a la actual")
    LocalDate fechaClase;
    @NotNull(message = "La hora de  la clase no puede estar vacia")
    LocalTime horaClase;
    @NotNull(message = "El nombre no puede estar vacio")
    Duration duracionClase;
    Profesor profesor;
    List<Alumno> alumnos;
}
