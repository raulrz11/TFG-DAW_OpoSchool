package com.example.tfgoposchool.services.usuarios;

import com.example.tfgoposchool.dtos.usuarios.UsuarioDto;
import com.example.tfgoposchool.dtos.usuarios.UsuarioUpdateDto;
import com.example.tfgoposchool.exceptions.UsuarioNotFoundException;
import com.example.tfgoposchool.mappers.usuarios.UsuariosMapper;
import com.example.tfgoposchool.models.Alumno;
import com.example.tfgoposchool.models.usuarios.Usuario;
import com.example.tfgoposchool.repositories.usuarios.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuariosServiceImpl implements UsuarioService{
    private final UsuariosRepository repository;
    private final UsuariosMapper mapper;

    @Autowired
    public UsuariosServiceImpl(UsuariosRepository repository, UsuariosMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Page<UsuarioDto> findAll(Optional<String> nombre, Optional<String> username,
                                    Optional<String> email, Optional<String> password,
                                    Optional<Usuario.Rol> rol, Pageable pageable) {

        Specification<Usuario> specNombre = (root, query, criteriaBuilder) ->
                nombre.map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + n + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Usuario> speUsername = (root, query, criteriaBuilder) ->
                username.map(u -> criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + u + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Usuario> specEmail = (root, query, criteriaBuilder) ->
                email.map(e -> criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + e + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Usuario> specPassword = (root, query, criteriaBuilder) ->
                password.map(p -> criteriaBuilder.like(criteriaBuilder.lower(root.get("password")), "%" + p + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Usuario> specRol = (root, query, criteriaBuilder) ->
                rol.map(r -> criteriaBuilder.isMember(r, root.get("rols")))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        //filtramos por defecto por isActive = true
        Specification<Usuario> specIsActive = (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("isActive"));

        Specification<Usuario> criterios =Specification.where(specNombre).and(speUsername).
                and(specEmail).and(specPassword).and(specRol).and(specIsActive);

        Page<UsuarioDto> lista = repository.findAll(criterios, pageable).map(mapper::toDto);

        return lista;
    }

    @Override
    public UsuarioDto findById(Long id) {
        Usuario existingUsuario = repository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("El usuario con id " + id + " no existe"));
        return mapper.toDto(existingUsuario);
    }

    @Override
    public UsuarioDto updateEmail(UsuarioUpdateDto dto, Long id) {
        Usuario existingUsuario = repository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("El usuario con id " + id + " no existe"));

        Alumno alumnoUsuario = existingUsuario.getAlumno();

        Usuario usuarioMapped = mapper.toEntity(dto, existingUsuario);
        alumnoUsuario.setEmail(usuarioMapped.getEmail());

        repository.save(usuarioMapped);

        return mapper.toDto(usuarioMapped);
    }

    @Override
    public UsuarioDto updatePassword(UsuarioUpdateDto dto, Long id) {
        Usuario existingUsuario = repository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("El usuario con id " + id + " no existe"));

        Usuario usuarioMapped = mapper.toEntity(dto, existingUsuario);

        repository.save(usuarioMapped);

        return mapper.toDto(usuarioMapped);
    }
}
