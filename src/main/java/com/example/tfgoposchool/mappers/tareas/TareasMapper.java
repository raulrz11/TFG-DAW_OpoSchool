package com.example.tfgoposchool.mappers.tareas;

import com.example.tfgoposchool.dtos.tareas.TareaCreateDto;
import com.example.tfgoposchool.dtos.tareas.TareaDto;
import com.example.tfgoposchool.dtos.tareas.TareaUpdateDto;
import com.example.tfgoposchool.models.Tarea;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TareasMapper {
    public Tarea toEntity(TareaCreateDto dto, String archivoUrl){
        Tarea tarea = Tarea.builder()
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .fechaEntrega(dto.getFechaEntrega())
                .archivoUrl(archivoUrl)
                .build();

        return tarea;
    }

    public Tarea toEntity(TareaUpdateDto dto, Tarea existingTarea, String archivoUrl){
        existingTarea.setTitulo(dto.getTitulo() != null ? dto.getTitulo() : existingTarea.getTitulo());
        existingTarea.setDescripcion(dto.getDescripcion() != null ? dto.getDescripcion() : existingTarea.getDescripcion());
        existingTarea.setFechaEntrega(dto.getFechaEntrega() != null ? dto.getFechaEntrega() : existingTarea.getFechaEntrega());
        existingTarea.setArchivoUrl(archivoUrl != null ? archivoUrl : existingTarea.getArchivoUrl());

        return existingTarea;
    }

    public TareaDto toDto(Tarea tarea){
        List<String> alumnoEntrega = (tarea.getEntregas() != null)
                ? tarea.getEntregas().stream()
                .map(entrega -> entrega.getAlumno().getNombre())
                .collect(Collectors.toList())
                : List.of();

        TareaDto dto = TareaDto.builder()
                .id(tarea.getId())
                .titulo(tarea.getTitulo())
                .descripcion(tarea.getDescripcion())
                .fechaEntrega(tarea.getFechaEntrega().toString())
                .archivoUrl(tarea.getArchivoUrl())
                .entregas(alumnoEntrega)
                .build();

        return dto;
    }
}
