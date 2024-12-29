package com.example.tfgoposchool.services.entregas;

import com.example.tfgoposchool.dtos.entregas.EntregaDto;
import com.example.tfgoposchool.dtos.entregas.EntregaUpdateDto;
import com.example.tfgoposchool.models.Entrega;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EntregasService {
    Page<EntregaDto> findAllByTareaId(Optional<Entrega.EstadoEntrega> estado, Optional<Double> calificacion,
                                      Optional<LocalDateTime> fechaEntrega, Long idTarea, Pageable pageable);

    EntregaDto findById(Long id);

    EntregaDto save(Long idTarea, Long idAlumno, MultipartFile archivo);

    EntregaDto update(Long id, MultipartFile archivo);

    EntregaDto correctEntrega(EntregaUpdateDto dto, Long id);

    void deleteById(Long id);
}
