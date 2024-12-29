package com.example.tfgoposchool.dtos.grupos;

import com.example.tfgoposchool.models.Alumno;
import com.example.tfgoposchool.models.Profesor;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class GrupoUpdateDto {
    @Size(min = 1, max = 30, message = "El nombre debe tener entre 1 y 30 caracteres")
    String nombre;
    @FutureOrPresent(message = "La fecha de la clase debe ser igual o superior a la actual")
    LocalDate fechaClase;
    LocalTime horaClase;
    Duration duracionClase;
    Profesor profesor;
    List<Alumno> alumnos;
}
