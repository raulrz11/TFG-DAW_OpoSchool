package com.example.tfgoposchool.mappers.alumnos;

import com.example.tfgoposchool.dtos.alumnos.AlumnoUpdateDto;
import com.example.tfgoposchool.dtos.alumnos.AlumnoCreateDto;
import com.example.tfgoposchool.dtos.alumnos.AlumnoDto;
import com.example.tfgoposchool.models.Alumno;
import com.example.tfgoposchool.models.subscripciones.Subscripcion;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AlumnosMapper {
    public Alumno toEntity(AlumnoCreateDto dto){
        Alumno alumno = Alumno.builder()
                .nombre(dto.getNombre())
                .apellidos(dto.getApellidos())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .dni(dto.getDni())
                .build();

        return alumno;
    }

    public Alumno toEntity(AlumnoUpdateDto dto, Alumno existingAlumno){
        existingAlumno.setNombre(dto.getNombre() != null ? dto.getNombre() : existingAlumno.getNombre());
        existingAlumno.setApellidos(dto.getApellidos() != null ? dto.getApellidos() : existingAlumno.getApellidos());
        existingAlumno.setEmail(existingAlumno.getEmail());
        existingAlumno.setTelefono(dto.getTelefono() != null ? dto.getTelefono() : existingAlumno.getTelefono());
        existingAlumno.setDni(dto.getDni() != null ? dto.getDni() : existingAlumno.getDni());
        existingAlumno.setUpdatedAt(LocalDateTime.now());

        return existingAlumno;
    }

    public AlumnoDto toDto(Alumno alumno){
        String estadoSubscripcion = alumno.getSubscripciones().stream()
                .filter(s -> s.getEstado() == Subscripcion.Estado.ACTIVA)
                .map(s -> s.getEstado().name())
                .findFirst()
                .orElse("SIN SUBSCRIPCION");

        AlumnoDto dto = AlumnoDto.builder()
                .id(alumno.getId())
                .nombre(alumno.getNombre())
                .apellidos(alumno.getApellidos())
                .email(alumno.getEmail())
                .telefono(alumno.getTelefono())
                .dni(alumno.getDni())
                .grupo(alumno.getGrupo() != null ? alumno.getGrupo().getNombre() : "SIN GRUPO")
                .usuario(alumno.getUsuario().getUsername())
                .subscripcion(estadoSubscripcion)
                .build();

        return dto;
    }
}
