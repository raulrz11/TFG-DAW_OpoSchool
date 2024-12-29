package com.example.tfgoposchool.services.alumnos;

import com.example.tfgoposchool.dtos.alumnos.AlumnoUpdateDto;
import com.example.tfgoposchool.dtos.alumnos.AlumnoCreateDto;
import com.example.tfgoposchool.dtos.alumnos.AlumnoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AlumnosService {
    Page<AlumnoDto> findAll(Optional<String> nombre, Optional<String> apellidos,
                            Optional<String> email, Optional<String> telefono,
                            Optional<String> dni, Pageable pageable);

    AlumnoDto findById(Long id);

    AlumnoDto save(AlumnoCreateDto dto);

    AlumnoDto update(AlumnoUpdateDto dto, Long id);

    void deleteById(Long id);
}
