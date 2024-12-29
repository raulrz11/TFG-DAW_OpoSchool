package com.example.tfgoposchool.repositories.alumnos;

import com.example.tfgoposchool.dtos.alumnos.AlumnoDto;
import com.example.tfgoposchool.models.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlumnosRepository extends JpaRepository<Alumno, Long>, JpaSpecificationExecutor<Alumno> {
    List<Alumno> findByIdNotIn(List<Long> excludedIds);
}
