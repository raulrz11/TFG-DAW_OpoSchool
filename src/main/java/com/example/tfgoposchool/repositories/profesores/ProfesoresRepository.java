package com.example.tfgoposchool.repositories.profesores;

import com.example.tfgoposchool.models.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfesoresRepository extends JpaRepository<Profesor, Long>, JpaSpecificationExecutor<Profesor> {
    Optional<Profesor> findByNombreEqualsIgnoreCase(String nombre);
}
