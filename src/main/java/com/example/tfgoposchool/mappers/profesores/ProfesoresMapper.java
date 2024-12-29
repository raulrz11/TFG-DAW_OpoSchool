package com.example.tfgoposchool.mappers.profesores;

import com.example.tfgoposchool.dtos.profesores.ProfesorCreateDto;
import com.example.tfgoposchool.dtos.profesores.ProfesorDto;
import com.example.tfgoposchool.dtos.profesores.ProfesorUpdateDto;
import com.example.tfgoposchool.models.Profesor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProfesoresMapper {
    public Profesor toEntity(ProfesorCreateDto dto){
        Profesor profesor = Profesor.builder()
                .nombre(dto.getNombre())
                .apellidos(dto.getApellidos())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .dni(dto.getDni())
                .titulosAcademicos(dto.getTitulosAcademicos())
                .build();

        return profesor;
    }

    public Profesor toEntity(ProfesorUpdateDto dto, Profesor existingProfesor){
        existingProfesor.setNombre(dto.getNombre() != null ? dto.getNombre() : existingProfesor.getNombre());
        existingProfesor.setApellidos(dto.getApellidos() != null ? dto.getApellidos() : existingProfesor.getApellidos());
        existingProfesor.setEmail(dto.getEmail() != null ? dto.getEmail() : existingProfesor.getEmail());
        existingProfesor.setTelefono(dto.getTelefono() != null ? dto.getTelefono() : existingProfesor.getTelefono());
        existingProfesor.setDni(dto.getDni() != null ? dto.getDni() : existingProfesor.getDni());
        existingProfesor.setTitulosAcademicos(dto.getTitulosAcademicos() != null ? dto.getTitulosAcademicos() : existingProfesor.getTitulosAcademicos());

        return existingProfesor;
    }

    public ProfesorDto toDto(Profesor profesor){
        List<String> nombreGrupos = (profesor.getGrupos() != null)
                ? profesor.getGrupos().stream()
                .map(grupo -> grupo.getNombre())
                .collect(Collectors.toList())
                : List.of();

        ProfesorDto dto = ProfesorDto.builder()
                .id(profesor.getId())
                .nombre(profesor.getNombre())
                .apellidos(profesor.getApellidos())
                .email(profesor.getEmail())
                .telefono(profesor.getTelefono())
                .dni(profesor.getDni())
                .titulosAcademicos(profesor.getTitulosAcademicos())
                .grupos(nombreGrupos)
                .build();

        return dto;
    }
}
