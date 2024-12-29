package com.example.tfgoposchool.services.grupos;

import com.example.tfgoposchool.dtos.grupos.GrupoCreateDto;
import com.example.tfgoposchool.dtos.grupos.GrupoDto;
import com.example.tfgoposchool.dtos.grupos.GrupoUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface GruposService {
    Page<GrupoDto> findAll(Optional<String> nombre, Optional<LocalDate> fechaClase,
                           Optional<LocalTime> horaClase, Optional<Duration> duracionClase,
                           Pageable pageable);

    Page<GrupoDto> findAllByProfesorId(Long id, Pageable pageable);

    GrupoDto findById(Long id);

    GrupoDto save(GrupoCreateDto dto);

    GrupoDto update(GrupoUpdateDto dto, Long id);

    void delteById(Long id);
}
