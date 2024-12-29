package com.example.tfgoposchool.repositories.usuarios;

import com.example.tfgoposchool.models.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {
}
