package com.example.tfgoposchool.services.usuarios;

import com.example.tfgoposchool.dtos.usuarios.UsuarioDto;
import com.example.tfgoposchool.dtos.usuarios.UsuarioUpdateDto;
import com.example.tfgoposchool.models.usuarios.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UsuarioService {
    Page<UsuarioDto> findAll(Optional<String> nombre, Optional<String> username,
                             Optional<String> email, Optional<String> password,
                             Optional<Usuario.Rol> rol, Pageable pageable);

    UsuarioDto findById(Long id);

    UsuarioDto updateEmail(UsuarioUpdateDto dto, Long id);

    UsuarioDto updatePassword(UsuarioUpdateDto dto, Long id);
}
