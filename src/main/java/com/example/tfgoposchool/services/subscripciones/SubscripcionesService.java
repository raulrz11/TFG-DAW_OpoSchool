package com.example.tfgoposchool.services.subscripciones;

import com.example.tfgoposchool.dtos.subscripciones.SubscripcionCreateDto;
import com.example.tfgoposchool.dtos.subscripciones.SubscripcionDto;
import com.example.tfgoposchool.models.subscripciones.Subscripcion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface SubscripcionesService {
    Page<SubscripcionDto> findAll(Optional<LocalDate> fechaInicio, Optional<LocalDate> fechaFin,
                                            Optional<Subscripcion.Estado> estado, Pageable pageable);

    Page<SubscripcionDto> findByAlumnoId(Long id, Pageable pageable);

    SubscripcionDto renewSubscription(SubscripcionCreateDto dto, Long idAlumno);

    void activateSubscription(Subscripcion existingSubscripcion);

    void automaticRenewSubscription(Subscripcion subscripcion);

    void softDelete(Long id);
}
