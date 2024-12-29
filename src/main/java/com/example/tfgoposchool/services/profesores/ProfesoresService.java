package com.example.tfgoposchool.services.profesores;

import com.example.tfgoposchool.dtos.profesores.ProfesorCreateDto;
import com.example.tfgoposchool.dtos.profesores.ProfesorDto;
import com.example.tfgoposchool.dtos.profesores.ProfesorUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProfesoresService {
    Page<ProfesorDto> findAll(Optional<String> nombre, Optional<String> apellidos,
                              Optional<String> email, Optional<String> telefono,
                              Optional<String> dni, Pageable pageable);

    ProfesorDto findById(Long id);

    ProfesorDto save(ProfesorCreateDto dto);

    ProfesorDto update(ProfesorUpdateDto dto, Long id);

    void delete (Long id);

    void deleteById (Long id);

}
