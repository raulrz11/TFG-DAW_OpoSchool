package com.example.tfgoposchool.services.grupos;

import com.example.tfgoposchool.dtos.alumnos.AlumnoDto;
import com.example.tfgoposchool.dtos.grupos.GrupoCreateDto;
import com.example.tfgoposchool.dtos.grupos.GrupoDto;
import com.example.tfgoposchool.dtos.grupos.GrupoUpdateDto;
import com.example.tfgoposchool.exceptions.AlumnoNotFound;
import com.example.tfgoposchool.exceptions.grupos.GrupoNotFound;
import com.example.tfgoposchool.exceptions.ProfesorNotFound;
import com.example.tfgoposchool.exceptions.grupos.GruposBadRequest;
import com.example.tfgoposchool.mappers.alumnos.AlumnosMapper;
import com.example.tfgoposchool.mappers.grupos.GruposMapper;
import com.example.tfgoposchool.models.Alumno;
import com.example.tfgoposchool.models.Grupo;
import com.example.tfgoposchool.models.Profesor;
import com.example.tfgoposchool.repositories.alumnos.AlumnosRepository;
import com.example.tfgoposchool.repositories.grupos.GruposRepository;
import com.example.tfgoposchool.repositories.profesores.ProfesoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GruposServiceImpl implements GruposService{
    private final GruposRepository repository;
    private final GruposMapper mapper;
    private final ProfesoresRepository profesoresRepository;
    private final AlumnosRepository alumnosRepository;
    private final AlumnosMapper alumnosMapper;

    @Autowired
    public GruposServiceImpl(GruposRepository repository, GruposMapper mapper, ProfesoresRepository profesoresRepository, AlumnosRepository alumnosRepository, AlumnosMapper alumnosMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.profesoresRepository = profesoresRepository;
        this.alumnosRepository = alumnosRepository;
        this.alumnosMapper = alumnosMapper;
    }


    @Override
    public Page<GrupoDto> findAll(Optional<String> nombre, Optional<LocalDate> fechaClase,
                                  Optional<LocalTime> horaClase, Optional<Duration> duracionClase,
                                  Pageable pageable) {

        Specification<Grupo> specNombre = (root, query, criteriaBuilder) ->
                nombre.map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + n + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Grupo> specFechaClase = (root, query, criteriaBuilder) ->
                fechaClase.map(f -> criteriaBuilder.equal(root.get("fechaClase"), f))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Grupo> specHoraClase = (root, query, criteriaBuilder) ->
                horaClase.map(h -> criteriaBuilder.equal(root.get("horaClase"), h))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Grupo> specDuracionClase = (root, query, criteriaBuilder) ->
                duracionClase.map(d -> criteriaBuilder.equal(root.get("duracionClase"), d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Grupo> criterios = Specification.where(specNombre).and(specFechaClase).and(specHoraClase).and(specDuracionClase);

        Page<GrupoDto> lista = repository.findAll(criterios, pageable).map(mapper::toDto);

        return lista;
    }

    @Override
    public Page<GrupoDto> findAllByProfesorId(Long id, Pageable pageable) {
        Page<GrupoDto> lista = repository.findByProfesorId(id, pageable).map(mapper::toDto);

        return lista;
    }

    public List<AlumnoDto> findAlumnosInGrupo(Long id){
        Grupo existingGrupo = repository.findById(id)
                .orElseThrow(() -> new GrupoNotFound("El grupo con id " + id + " no existe"));

        return existingGrupo.getAlumnos().stream().map(alumnosMapper::toDto).toList();
    }

    public List<AlumnoDto> findAlumnosInNoGrupo(Long id){
        Grupo existingGrupo = repository.findById(id)
                .orElseThrow(() -> new GrupoNotFound("El grupo con id " + id + " no existe"));

        List<Long> idsAlumnosGrupo = existingGrupo.getAlumnos().stream()
                .map(Alumno::getId)
                .toList();

        if (idsAlumnosGrupo.isEmpty()){
            return alumnosRepository.findAll().stream().map(alumnosMapper::toDto).toList();
        }else {
            return alumnosRepository.findByIdNotIn(idsAlumnosGrupo).stream().map(alumnosMapper::toDto).toList();
        }
    }

    @Override
    public GrupoDto findById(Long id) {
        Grupo existingGrupo = repository.findById(id)
                .orElseThrow(() -> new GrupoNotFound("El grupo con id " + id + " no existe"));

        return mapper.toDto(existingGrupo);
    }

    public GrupoDto findByAlumnoId(Long alumnoId) {
        Grupo existingGrupo = repository.findByAlumnoId(alumnoId)
                .orElseThrow(() -> new GrupoNotFound("El grupo con id del alumno " + alumnoId + " no existe"));

        return mapper.toDto(existingGrupo);
    }

    @Override
    public GrupoDto save(GrupoCreateDto dto) {
        if (!repository.existsByNombre(dto.getNombre())){
            Grupo grupoMapped = mapper.toEntity(dto);
            if (dto.getProfesor() != null){
                Profesor existingProfesor = profesoresRepository.findById(dto.getProfesor().getId())
                        .orElseThrow(() -> new ProfesorNotFound("El profesor con id " + dto.getProfesor().getId() + " no existe"));
                existingProfesor.getGrupos().add(grupoMapped);
                grupoMapped.setProfesor(existingProfesor);
            }
            if (dto.getAlumnos() != null && !dto.getAlumnos().isEmpty()){
                List<Alumno> alumnos = dto.getAlumnos().stream()
                        .map(alumno -> alumnosRepository.findById(alumno.getId())
                                .orElseThrow(() -> new AlumnoNotFound("EL alumno con id " + alumno.getId() + " no existe")))
                        .collect(Collectors.toList());

                //tengo que meter una ventana de confirmacion si tienen grupo
                //validacion: no puedes meter el mismo alumno
                grupoMapped.setAlumnos(alumnos);
                alumnos.forEach(alumno -> alumno.setGrupo(grupoMapped));
            }

            repository.save(grupoMapped);
            return mapper.toDto(grupoMapped);
        }else {
            throw new GruposBadRequest("Ya existe un grupo con ese nombre");
        }
    }

    @Override
    public GrupoDto update(GrupoUpdateDto dto, Long id) {
        Grupo existingGrupo = repository.findById(id)
                .orElseThrow(() -> new GrupoNotFound("El grupo con id " + id + " no existe"));

        Profesor existingProfesor = null;
        if (dto.getProfesor() != null){
            existingProfesor = profesoresRepository.findById(dto.getProfesor().getId())
                    .orElseThrow(() -> new ProfesorNotFound("El profesor con id " + dto.getProfesor().getId() + " no existe"));
        }

        Grupo grupoMapped = mapper.toEntity(dto, existingGrupo, existingProfesor);
        Grupo updatedGrupo = repository.save(grupoMapped);

        return mapper.toDto(updatedGrupo);
    }

    @Override
    public void delteById(Long id) {
        Grupo existingGrupo = repository.findById(id)
                .orElseThrow(() -> new GrupoNotFound("El grupo con id " + id + " no existe"));

        existingGrupo.getAlumnos().forEach(alumno -> {
            alumno.setGrupo(null);
        });
        alumnosRepository.saveAll(existingGrupo.getAlumnos());

        repository.deleteById(id);
    }

    public void addAlumno(Long alumnoId, Long grupoId){
        Alumno existingAlumno = alumnosRepository.findById(alumnoId)
                .orElseThrow(() -> new AlumnoNotFound("El alumno con id " + alumnoId + " no existe"));
        Grupo existingGrupo = repository.findById(grupoId)
                .orElseThrow(() -> new GrupoNotFound("El grupo con id " + grupoId + " no existe"));

        Grupo grupoAlumno = existingAlumno.getGrupo();

        if (grupoAlumno != null){
            if (!existingGrupo.getAlumnos().contains(existingAlumno)){
                grupoAlumno.getAlumnos().remove(existingAlumno);
                existingAlumno.setGrupo(null);
                existingGrupo.getAlumnos().add(existingAlumno);
                existingAlumno.setGrupo(existingGrupo);
                repository.save(grupoAlumno);
            }else {
                throw new GruposBadRequest("EL alumno ya esta en el grupo");
            }
        }else {
            existingGrupo.getAlumnos().add(existingAlumno);
            existingAlumno.setGrupo(existingGrupo);
        }

        repository.save(existingGrupo);

    }

    public void removeAlumno(Long alumnoId){
        Alumno existingAlumno = alumnosRepository.findById(alumnoId)
                .orElseThrow(() -> new AlumnoNotFound("El alumno con id " + alumnoId + " no existe"));

        Grupo grupoAlumno = existingAlumno.getGrupo();
        grupoAlumno.getAlumnos().remove(existingAlumno);
        existingAlumno.setGrupo(null);
        repository.save(grupoAlumno);
    }
}
