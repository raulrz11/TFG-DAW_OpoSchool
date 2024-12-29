package com.example.tfgoposchool.mappers.entregas;

import com.example.tfgoposchool.dtos.entregas.EntregaDto;
import com.example.tfgoposchool.dtos.entregas.EntregaUpdateDto;
import com.example.tfgoposchool.models.Entrega;
import org.springframework.stereotype.Component;

@Component
public class EntregasMapper {
    public Entrega toEntity(String archivoUrl){
        Entrega entrega = Entrega.builder()
                .archivoUrl(archivoUrl)
                .build();

        return entrega;
    }

    public Entrega toEntity(String archivoUrl, Entrega existingEntrega){
        existingEntrega.setArchivoUrl(archivoUrl);

        return existingEntrega;
    }

    public Entrega toEntityCorrect(EntregaUpdateDto dto, Entrega existingEntrega){
        existingEntrega.setEstado(Entrega.EstadoEntrega.CORREGIDA);
        existingEntrega.setFechaEntrega(existingEntrega.getFechaEntrega());
        existingEntrega.setArchivoUrl(existingEntrega.getArchivoUrl());
        existingEntrega.setCalificacion(dto.getCalificacion());
        existingEntrega.setComentarios(dto.getComentarios());

        return existingEntrega;
    }

    public EntregaDto toDto(Entrega entrega){
        EntregaDto dto = EntregaDto.builder()
                .id(entrega.getId())
                .archivoUrl(entrega.getArchivoUrl())
                .estado(entrega.getEstado().name())
                .fechaEntrega(entrega.getFechaEntrega().toString())
                .calificacion(entrega.getCalificacion() != null ? entrega.getCalificacion() : 0)
                .comentarios(entrega.getComentarios() != null ? entrega.getComentarios() : "Sin comentarios")
                .tarea(entrega.getTarea().getTitulo())
                .alumno(entrega.getAlumno().getNombre())
                .build();

        return dto;
    }
}
