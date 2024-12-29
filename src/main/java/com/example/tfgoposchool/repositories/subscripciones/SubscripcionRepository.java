package com.example.tfgoposchool.repositories.subscripciones;

import com.example.tfgoposchool.dtos.subscripciones.SubscripcionDto;
import com.example.tfgoposchool.models.subscripciones.Subscripcion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SubscripcionRepository extends JpaRepository<Subscripcion, Long>, JpaSpecificationExecutor<Subscripcion> {
    Page<Subscripcion> findByAlumnoId(Long idAlumno, Pageable pageable);

    List<Subscripcion> findByFechaFin(LocalDate fechaFin);

    List<Subscripcion> findByFechaInicioAndEstado(LocalDate fechaInicio, Subscripcion.Estado estado);
}
