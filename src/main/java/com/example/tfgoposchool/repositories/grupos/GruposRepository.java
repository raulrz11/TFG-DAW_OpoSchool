package com.example.tfgoposchool.repositories.grupos;

import com.example.tfgoposchool.models.Grupo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GruposRepository extends JpaRepository<Grupo, Long>, JpaSpecificationExecutor<Grupo> {
    boolean existsByNombre(String nombre);

    Page<Grupo> findByProfesorId(Long id, Pageable pageable);

    @Query("SELECT g FROM Grupo g JOIN g.alumnos a WHERE a.id = :alumnoId")
    Optional<Grupo> findByAlumnoId(Long alumnoId);
}
