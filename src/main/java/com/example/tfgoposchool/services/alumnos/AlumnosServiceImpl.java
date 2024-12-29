package com.example.tfgoposchool.services.alumnos;

import com.example.tfgoposchool.auth.exceptions.EmailAlreadyExists;
import com.example.tfgoposchool.auth.repository.AuthUsersRepository;
import com.example.tfgoposchool.dtos.alumnos.AlumnoUpdateDto;
import com.example.tfgoposchool.dtos.alumnos.AlumnoCreateDto;
import com.example.tfgoposchool.dtos.alumnos.AlumnoDto;
import com.example.tfgoposchool.exceptions.AlumnoNotFound;
import com.example.tfgoposchool.mappers.alumnos.AlumnosMapper;
import com.example.tfgoposchool.mappers.subscripciones.SubscripcionesMapper;
import com.example.tfgoposchool.mappers.usuarios.UsuariosMapper;
import com.example.tfgoposchool.models.Alumno;
import com.example.tfgoposchool.models.subscripciones.Subscripcion;
import com.example.tfgoposchool.models.usuarios.Usuario;
import com.example.tfgoposchool.repositories.alumnos.AlumnosRepository;
import com.example.tfgoposchool.repositories.subscripciones.SubscripcionRepository;
import com.example.tfgoposchool.repositories.usuarios.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlumnosServiceImpl implements AlumnosService{
    private final AlumnosRepository repository;
    private final AlumnosMapper mapper;
    private final UsuariosMapper usuariosMapper;
    private final UsuariosRepository usuariosRepository;
    private final SubscripcionRepository subscripcionRepository;
    private final SubscripcionesMapper subscripcionesMapper;
    private final AuthUsersRepository authUsersRepository;

    @Autowired
    public AlumnosServiceImpl(AlumnosRepository repository, AlumnosMapper mapper, UsuariosMapper usuariosMapper, UsuariosRepository usuariosRepository, SubscripcionRepository subscripcionRepository, SubscripcionesMapper subscripcionesMapper, AuthUsersRepository authUsersRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.usuariosMapper = usuariosMapper;
        this.usuariosRepository = usuariosRepository;
        this.subscripcionRepository = subscripcionRepository;
        this.subscripcionesMapper = subscripcionesMapper;
        this.authUsersRepository = authUsersRepository;
    }

    @Override
    public Page<AlumnoDto> findAll(Optional<String> nombre, Optional<String> apellidos,
                                   Optional<String> email, Optional<String> telefono,
                                   Optional<String> dni, Pageable pageable) {

        Specification<Alumno> specNombre = (root, query, criteriaBuilder) ->
                nombre.map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + n + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Alumno> specApellidos = (root, query, criteriaBuilder) ->
                apellidos.map(a -> criteriaBuilder.like(criteriaBuilder.lower(root.get("apellidos")), "%" + a + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Alumno> specEmail = (root, query, criteriaBuilder) ->
                email.map(e -> criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + e + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Alumno> specTelefono = (root, query, criteriaBuilder) ->
                telefono.map(t -> criteriaBuilder.like(criteriaBuilder.lower(root.get("telefono")), "%" + t + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Alumno> specDni = (root, query, criteriaBuilder) ->
                dni.map(d -> criteriaBuilder.like(criteriaBuilder.lower(root.get("dni")), "%" + d + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        //filtramos por defecto por isDeleted = false
        Specification<Alumno> specIsDeleted = (root, query, criteriaBuilder) ->
                criteriaBuilder.isFalse(root.get("isDeleted"));

        Specification<Alumno> criterios =Specification.where(specNombre).and(specApellidos).
                and(specEmail).and(specTelefono).and(specDni).and(specIsDeleted);

        Page<AlumnoDto> lista = repository.findAll(criterios, pageable).map(mapper::toDto);

        return lista;
    }

    @Override
    public AlumnoDto findById(Long id) {
        Alumno existingAlumno = repository.findById(id)
                .orElseThrow(() -> new AlumnoNotFound("El alumno con id " + id + " no existe"));

        return mapper.toDto(existingAlumno);
    }

    @Override
    public AlumnoDto save(AlumnoCreateDto dto) {
        if (!authUsersRepository.existsByEmail(dto.getEmail())){
            Alumno alumnoMapped = mapper.toEntity(dto);
            Usuario newUsuario = usuariosMapper.newUsuarioFromAlumno(alumnoMapped);
            usuariosRepository.save(newUsuario);

            //Creacion de la subscripcion
            Subscripcion newSubscripcion = subscripcionesMapper.toEntity(dto);
            subscripcionRepository.save(newSubscripcion);

            //Agregamos relaciones y guardamos alumno
            newSubscripcion.setAlumno(alumnoMapped);
            alumnoMapped.setUsuario(newUsuario);
            alumnoMapped.setSubscripciones(List.of(newSubscripcion));

            Alumno newAlumno = repository.save(alumnoMapped);

            return mapper.toDto(newAlumno);
        }else {
            throw new EmailAlreadyExists("El email ya existe");
        }
    }

    @Override
    public AlumnoDto update(AlumnoUpdateDto dto, Long id) {
        Alumno existingAlumno = repository.findById(id)
                .orElseThrow(() -> new AlumnoNotFound("El alumno con id " + id + " no existe"));

        Alumno alumnoMapped = mapper.toEntity(dto, existingAlumno);
        Alumno updatedAlumno = repository.save(alumnoMapped);

        return mapper.toDto(updatedAlumno);
    }

    @Override
    public void deleteById(Long id) {
        Alumno existingAlumno = repository.findById(id)
                .orElseThrow(() -> new AlumnoNotFound("El alumno con id " + id + " no existe"));

        repository.deleteById(id);
    }
}
