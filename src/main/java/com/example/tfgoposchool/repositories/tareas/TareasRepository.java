package com.example.tfgoposchool.repositories.tareas;

import com.example.tfgoposchool.models.Tarea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TareasRepository extends JpaRepository<Tarea, Long>, JpaSpecificationExecutor<Tarea> {
    Page<Tarea> findByGrupoId(Long idGrupo, Pageable pageable);
}
