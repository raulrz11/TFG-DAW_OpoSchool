package com.example.tfgoposchool.mappers.grupos;

import com.example.tfgoposchool.dtos.grupos.GrupoCreateDto;
import com.example.tfgoposchool.dtos.grupos.GrupoDto;
import com.example.tfgoposchool.dtos.grupos.GrupoUpdateDto;
import com.example.tfgoposchool.models.Alumno;
import com.example.tfgoposchool.models.Grupo;
import com.example.tfgoposchool.models.Profesor;
import com.example.tfgoposchool.models.Tarea;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GruposMapper {
    public Grupo toEntity(GrupoCreateDto dto){
        Grupo grupo = Grupo.builder()
                .nombre(dto.getNombre())
                .fechaClase(dto.getFechaClase())
                .horaClase(dto.getHoraClase())
                .duracionClase(dto.getDuracionClase())
                .build();

        return grupo;
    }

    public Grupo toEntity(GrupoUpdateDto dto, Grupo existingGrupo, Profesor existingProfesor){
        existingGrupo.setNombre(dto.getNombre() != null ? dto.getNombre() : existingGrupo.getNombre());
        existingGrupo.setFechaClase(dto.getFechaClase() != null ? dto.getFechaClase() : existingGrupo.getFechaClase());
        existingGrupo.setHoraClase(dto.getHoraClase() != null ? dto.getHoraClase() : existingGrupo.getHoraClase());
        existingGrupo.setDuracionClase(dto.getDuracionClase() != null ? dto.getDuracionClase() : existingGrupo.getDuracionClase());
        existingGrupo.setProfesor(dto.getProfesor() != null ? existingProfesor : existingGrupo.getProfesor());

        return existingGrupo;
    }

    public GrupoDto toDto(Grupo grupo){
        /*List<Object[]> alumnosDetalles = (grupo.getAlumnos() != null)
                ? grupo.getAlumnos().stream()
                .map(alumno -> new Object[] { alumno.getId(), alumno.getNombre() + " " + alumno.getApellidos() })
                .collect(Collectors.toList())
                : List.of();*/

        List<String> nombreAlumnos = (grupo.getAlumnos() != null)
                ? grupo.getAlumnos().stream()
                .map(Alumno::getNombre)
                .collect(Collectors.toList())
                : List.of();

        List<String> tituloTareas = (grupo.getTareas() != null)
                ? grupo.getTareas().stream()
                .map(Tarea::getTitulo)
                .collect(Collectors.toList())
                : List.of();

        GrupoDto dto = GrupoDto.builder()
                .id(grupo.getId())
                .nombre(grupo.getNombre())
                .fechaClase(grupo.getFechaClase().toString())
                .horaClase(grupo.getHoraClase().toString())
                .duracionClase(grupo.getDuracionClase().toString())
                .profesor(grupo.getProfesor() != null ? grupo.getProfesor().getNombre() : "SIN PROFESOR")
                .alumnos(nombreAlumnos)
                .tareas(tituloTareas)
                .build();

        return dto;
    }
}
