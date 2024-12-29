package com.example.tfgoposchool.services.entregas;

import com.example.tfgoposchool.dtos.entregas.EntregaDto;
import com.example.tfgoposchool.dtos.entregas.EntregaUpdateDto;
import com.example.tfgoposchool.exceptions.AlumnoNotFound;
import com.example.tfgoposchool.exceptions.entregas.EntregaBadRequest;
import com.example.tfgoposchool.exceptions.entregas.EntregaNotFound;
import com.example.tfgoposchool.exceptions.grupos.GruposBadRequest;
import com.example.tfgoposchool.exceptions.tareas.TareaNotFound;
import com.example.tfgoposchool.mappers.entregas.EntregasMapper;
import com.example.tfgoposchool.models.Alumno;
import com.example.tfgoposchool.models.Entrega;
import com.example.tfgoposchool.models.Grupo;
import com.example.tfgoposchool.models.Tarea;
import com.example.tfgoposchool.repositories.alumnos.AlumnosRepository;
import com.example.tfgoposchool.repositories.entregas.EntregasRepository;
import com.example.tfgoposchool.repositories.tareas.TareasRepository;
import com.example.tfgoposchool.storage.service.FileSystemStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EntregasServiceImpl implements EntregasService{
    private final EntregasRepository repository;
    private final EntregasMapper mapper;
    private final TareasRepository tareasRepository;
    private final AlumnosRepository alumnosRepository;
    private final FileSystemStorageService storageService;

    @Autowired
    public EntregasServiceImpl(EntregasRepository repository, EntregasMapper mapper, TareasRepository tareasRepository, AlumnosRepository alumnosRepository, FileSystemStorageService storageService) {
        this.repository = repository;
        this.mapper = mapper;
        this.tareasRepository = tareasRepository;
        this.alumnosRepository = alumnosRepository;
        this.storageService = storageService;
    }

    @Override
    public Page<EntregaDto> findAllByTareaId(Optional<Entrega.EstadoEntrega> estado,
                                             Optional<Double> calificacion,
                                             Optional<LocalDateTime> fechaEntrega,
                                             Long idTarea, Pageable pageable) {

        Specification<Entrega> specEstado = (root, query, criteriaBuilder) ->
                estado.map(e -> criteriaBuilder.equal(root.get("estado"), e))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Entrega> specCalificacion = (root, query, criteriaBuilder) ->
                calificacion.map(c -> criteriaBuilder.equal(root.get("calificacion"), c))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Entrega> specFechaEntrega = (root, query, criteriaBuilder) ->
                fechaEntrega.map(f -> criteriaBuilder.lessThanOrEqualTo(root.get("fechaEntrega"), f))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        //Filtramos por el id de la tarea
        Specification<Entrega> specTarea = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("tarea").get("id"), idTarea);

        Specification<Entrega> criterios = Specification.where(specEstado).and(specCalificacion).and(specFechaEntrega).and(specTarea);

        Page<EntregaDto> lista = repository.findAll(criterios, pageable).map(mapper::toDto);

        return lista;
    }

    @Override
    public EntregaDto findById(Long id) {
        Entrega existingEntrega = repository.findById(id)
                .orElseThrow(() -> new EntregaNotFound("La entrega con id " + id + " no existe"));

        return mapper.toDto(existingEntrega);
    }

    public EntregaDto findByTareaAndAlumno(Long tareaId, Long alumnoId){
        Entrega exisistingEntrega = repository.findByTareaIdAndAlumnoId(tareaId, alumnoId)
                .orElseThrow(() -> new EntregaNotFound("La entrega con id de tarea " + tareaId + " y con id de alumno " + alumnoId + " no existe"));

    return mapper.toDto(exisistingEntrega);
    }

    @Override
    public EntregaDto save(Long idTarea, Long idAlumno, MultipartFile archivo) {
        Tarea existingTarea = tareasRepository.findById(idTarea)
                .orElseThrow(() -> new TareaNotFound("La tarea con id " + idTarea + " no existe"));
        Alumno existingAlumno = alumnosRepository.findById(idAlumno)
                .orElseThrow(() -> new AlumnoNotFound("El alumno con id " + idAlumno + " no existe"));
        Grupo existingGrupo = existingTarea.getGrupo();

        if (existingGrupo.getAlumnos().contains(existingAlumno)){
            boolean entregaAlreadyExists = repository.existsByTareaIdAndAlumnoId(idTarea, idAlumno);
            if (!entregaAlreadyExists){
                if (existingTarea.getFechaEntrega().isAfter(LocalDate.now())){
                    String archivoUrl = addFile(null, archivo, true);

                    Entrega entregaMapped = mapper.toEntity(archivoUrl);
                    entregaMapped.setTarea(existingTarea);
                    entregaMapped.setAlumno(existingAlumno);
                    existingTarea.getEntregas().add(entregaMapped);
                    existingAlumno.getEntregas().add(entregaMapped);

                    repository.save(entregaMapped);

                    return mapper.toDto(entregaMapped);
                }else {
                    throw new EntregaBadRequest("El plazo para hacer la entrega ya ha expirado");
                }
            }else {
                throw new EntregaBadRequest("Ya has hecho la entrega de esta tarea");
            }
        }else {
            throw new GruposBadRequest("Error al hacer la entrega: El alumno no pertenece a este grupo");
        }
    }

    @Override
    public EntregaDto update(Long id, MultipartFile archivo) {
        Entrega existingEntrega = repository.findById(id)
                .orElseThrow(() -> new EntregaNotFound("La entrega con id " + id + " no existe"));

        if (existingEntrega.getTarea().getFechaEntrega().isAfter(LocalDate.now())){
            if (existingEntrega.getEstado() != Entrega.EstadoEntrega.CORREGIDA){
                String archivoUrl = addFile(id, archivo, true);

                Entrega entregaMapped = mapper.toEntity(archivoUrl, existingEntrega);

                repository.save(entregaMapped);

                return mapper.toDto(entregaMapped);
            }else {
                throw new EntregaBadRequest("La entrega ya esta corregida");
            }
        }else {
            throw new EntregaBadRequest("El plazo para hacer la entrega ya ha expirado");
        }
    }

    @Override
    public EntregaDto correctEntrega(EntregaUpdateDto dto, Long id) {
        Entrega existingEntrega = repository.findById(id)
                .orElseThrow(() -> new EntregaNotFound("La entrega con id " + id + " no existe"));

        Entrega entregaMapped = mapper.toEntityCorrect(dto, existingEntrega);

        repository.save(entregaMapped);

        return mapper.toDto(entregaMapped);
    }

    @Override
    public void deleteById(Long id) {
        Entrega existingEntrega = repository.findById(id)
                .orElseThrow(() -> new EntregaNotFound("La entrega con id " + id + " no existe"));

        if (existingEntrega.getArchivoUrl() != null){
            storageService.delete(existingEntrega.getArchivoUrl());
        }

        repository.deleteById(id);
    }

    private String addFile(Long id, MultipartFile archivo, Boolean withUrl){
        if (id != null){
            Entrega existingEntrega = repository.findById(id)
                    .orElseThrow(() -> new EntregaNotFound("La entrega con id " + id + " no existe"));

            if (existingEntrega.getArchivoUrl() != null){
                storageService.delete(existingEntrega.getArchivoUrl());
            }

            String archivoStored = storageService.store(archivo);

            String archivoUrl = !withUrl ? archivoStored : storageService.getUrl(archivoStored);

            return archivoUrl;
        }else {
            String archivoStored = storageService.store(archivo);

            String archivoUrl = !withUrl ? archivoStored : storageService.getUrl(archivoStored);

            return archivoUrl;
        }
    }
}
