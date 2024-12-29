package com.example.tfgoposchool.mappers.subscripciones;

import com.example.tfgoposchool.dtos.alumnos.AlumnoCreateDto;
import com.example.tfgoposchool.dtos.subscripciones.SubscripcionCreateDto;
import com.example.tfgoposchool.dtos.subscripciones.SubscripcionDto;
import com.example.tfgoposchool.models.Alumno;
import com.example.tfgoposchool.models.subscripciones.Subscripcion;
import com.example.tfgoposchool.models.subscripciones.Tarjeta;
import org.springframework.stereotype.Component;

@Component
public class SubscripcionesMapper {
    public Subscripcion toEntity(SubscripcionCreateDto dto){
        Tarjeta tarjeta = Tarjeta.builder()
                .numTarjeta(dto.getTarjeta().getNumTarjeta())
                .fechaExpiracion(dto.getTarjeta().getFechaExpiracion())
                .codigoSeguridad(dto.getTarjeta().getCodigoSeguridad())
                .build();

        Subscripcion subscripcion = Subscripcion.builder()
                .fechaInicio(Subscripcion.getPrimerDiaMesSiguiente())
                .fechaFin(Subscripcion.getPrimerDiaMesSiguiente().plusDays(30))
                .estado(Subscripcion.Estado.PENDIENTE)
                .tarjetaCredito(tarjeta)
                .build();

        return subscripcion;
    }

    public Subscripcion toEntity(AlumnoCreateDto dto){
        Tarjeta tarjeta = Tarjeta.builder()
                .numTarjeta(dto.getTarjeta().getNumTarjeta())
                .fechaExpiracion(dto.getTarjeta().getFechaExpiracion())
                .codigoSeguridad(dto.getTarjeta().getCodigoSeguridad())
                .build();

        Subscripcion subscripcion = Subscripcion.builder()
                .fechaInicio(Subscripcion.getPrimerDiaMesSiguiente())
                .fechaFin(Subscripcion.getPrimerDiaMesSiguiente().plusDays(30))
                .estado(Subscripcion.Estado.PENDIENTE)
                .tarjetaCredito(tarjeta)
                .build();

        return subscripcion;
    }

    public SubscripcionDto toDto(Subscripcion subscripcion){
        SubscripcionDto dto = SubscripcionDto.builder()
                .id(subscripcion.getId())
                .alumnoId(subscripcion.getAlumno().getId())
                .alumno(subscripcion.getAlumno().getNombre())
                .fechaInicio(subscripcion.getFechaInicio().toString())
                .fechaFin(subscripcion.getFechaFin().toString())
                .estado(subscripcion.getEstado().name())
                .precio(subscripcion.getPrecio())
                .createdAt(subscripcion.getCreatedAt().toString())
                .updatedAt(subscripcion.getUpdatedAt().toString())
                .deletedAt(subscripcion.getDeletedAt() != null ? subscripcion.getDeletedAt().toString() : "Sin finalizar")
                .build();

        return dto;
    }
}
