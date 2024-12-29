package com.example.tfgoposchool.services.profesores;

import com.example.tfgoposchool.auth.exceptions.EmailAlreadyExists;
import com.example.tfgoposchool.auth.repository.AuthUsersRepository;
import com.example.tfgoposchool.dtos.profesores.ProfesorCreateDto;
import com.example.tfgoposchool.dtos.profesores.ProfesorDto;
import com.example.tfgoposchool.dtos.profesores.ProfesorUpdateDto;
import com.example.tfgoposchool.exceptions.ProfesorNotFound;
import com.example.tfgoposchool.mappers.profesores.ProfesoresMapper;
import com.example.tfgoposchool.mappers.usuarios.UsuariosMapper;
import com.example.tfgoposchool.models.Profesor;
import com.example.tfgoposchool.models.usuarios.Usuario;
import com.example.tfgoposchool.repositories.grupos.GruposRepository;
import com.example.tfgoposchool.repositories.profesores.ProfesoresRepository;
import com.example.tfgoposchool.repositories.usuarios.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfesoresServiceImpl implements ProfesoresService{
    private final ProfesoresRepository repository;
    private final ProfesoresMapper mapper;
    private final UsuariosMapper usuariosMapper;
    private final UsuariosRepository usuariosRepository;
    private final GruposRepository gruposRepository;
    private final AuthUsersRepository authUsersRepository;

    @Autowired
    public ProfesoresServiceImpl(ProfesoresRepository repository, ProfesoresMapper mapper, UsuariosMapper usuariosMapper, UsuariosRepository usuariosRepository, GruposRepository gruposRepository, AuthUsersRepository authUsersRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.usuariosMapper = usuariosMapper;
        this.usuariosRepository = usuariosRepository;
        this.gruposRepository = gruposRepository;
        this.authUsersRepository = authUsersRepository;
    }


    @Override
    public Page<ProfesorDto> findAll(Optional<String> nombre, Optional<String> apellidos,
                                     Optional<String> email, Optional<String> telefono,
                                     Optional<String> dni, Pageable pageable) {

        Specification<Profesor> specNombre = (root, query, criteriaBuilder) ->
                nombre.map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + n + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Profesor> specApellidos = (root, query, criteriaBuilder) ->
                apellidos.map(a -> criteriaBuilder.like(criteriaBuilder.lower(root.get("apellidos")), "%" + a + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Profesor> specEmail = (root, query, criteriaBuilder) ->
                email.map(e -> criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + e + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Profesor> specTelefono = (root, query, criteriaBuilder) ->
                telefono.map(t -> criteriaBuilder.like(criteriaBuilder.lower(root.get("telefono")), "%" + t + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Profesor> specDni = (root, query, criteriaBuilder) ->
                dni.map(d -> criteriaBuilder.like(criteriaBuilder.lower(root.get("dni")), "%" + d + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Profesor> specIsDeleted = (root, query, criteriaBuilder) ->
                criteriaBuilder.isFalse(root.get("isDeleted"));

        Specification<Profesor> criterios =Specification.where(specNombre).and(specApellidos).
                and(specEmail).and(specTelefono).and(specDni).and(specIsDeleted);

        Page<ProfesorDto> lista = repository.findAll(criterios, pageable).map(mapper::toDto);

        return lista;
    }

    @Override
    public ProfesorDto findById(Long id) {
        Profesor existingProfesor = repository.findById(id)
                .orElseThrow(() -> new ProfesorNotFound("El profesor con id " + id + " no existe"));

        return mapper.toDto(existingProfesor);
    }

    public ProfesorDto findByNombre(String nombre) {
        Profesor existingProfesor = repository.findByNombreEqualsIgnoreCase(nombre)
                .orElseThrow(() -> new ProfesorNotFound("El profesor " + nombre + " no existe"));

        return mapper.toDto(existingProfesor);
    }

    @Override
    public ProfesorDto save(ProfesorCreateDto dto) {
        if (!authUsersRepository.existsByEmail(dto.getEmail())){
            Profesor profesorMapped = mapper.toEntity(dto);

            Usuario newUsuario = usuariosMapper.newUsuarioFromProfesor(profesorMapped);
            usuariosRepository.save(newUsuario);
            profesorMapped.setUsuario(newUsuario);

            Profesor newProfesor = repository.save(profesorMapped);

            return mapper.toDto(newProfesor);
        }else{
            throw new EmailAlreadyExists("El email ya existe");
        }
    }

    @Override
    public ProfesorDto update(ProfesorUpdateDto dto, Long id) {
        Profesor existingProfesor = repository.findById(id)
                .orElseThrow(() -> new ProfesorNotFound("El profesor con id " + id + " no existe"));

        Profesor profesorMapped = mapper.toEntity(dto, existingProfesor);
        Profesor updatedProfesor = repository.save(profesorMapped);

        return mapper.toDto(updatedProfesor);
    }

    @Override
    public void delete(Long id) {
        Profesor existingProfesor = repository.findById(id)
                .orElseThrow(() -> new ProfesorNotFound("El profesor con id " + id + " no existe"));

        repository.deleteById(id);
    }

    @Override
    public void deleteById(Long id) {
        Profesor existingProfesor = repository.findById(id)
                .orElseThrow(() -> new ProfesorNotFound("El profesor con id " + id + " no existe"));

        existingProfesor.getGrupos().forEach(grupo -> {
            grupo.setProfesor(null);
        });
        gruposRepository.saveAll(existingProfesor.getGrupos());

        repository.deleteById(id);
    }
}
