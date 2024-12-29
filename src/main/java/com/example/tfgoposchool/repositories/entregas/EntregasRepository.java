package com.example.tfgoposchool.repositories.entregas;

import com.example.tfgoposchool.models.Entrega;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntregasRepository extends JpaRepository<Entrega, Long>, JpaSpecificationExecutor<Entrega> {
    boolean existsByTareaIdAndAlumnoId(Long idTarea, Long idAlumno);

    Optional<Entrega> findByTareaIdAndAlumnoId(Long tareaId, Long alumnoId);
}
