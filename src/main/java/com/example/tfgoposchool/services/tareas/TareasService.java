package com.example.tfgoposchool.services.tareas;

import com.example.tfgoposchool.dtos.tareas.TareaCreateDto;
import com.example.tfgoposchool.dtos.tareas.TareaDto;
import com.example.tfgoposchool.dtos.tareas.TareaUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

public interface TareasService {
    Page<TareaDto> findAll(Optional<String> titulo, Optional<String> descripcion,
                           Optional<LocalDate> fechaEntrega, Optional<String> archivoUrl,
                           Pageable pageable);

    Page<TareaDto> findAllByGrupoId(Long grupoId, Pageable pageable);

    TareaDto findById(Long id);

    TareaDto save(TareaCreateDto dto, Long grupoId, MultipartFile archivo);

    TareaDto update(TareaUpdateDto dto, Long id, MultipartFile archivo);

    void deleteById(Long id);
}
