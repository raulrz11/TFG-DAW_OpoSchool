package com.example.tfgoposchool.services.tareas;

import com.example.tfgoposchool.dtos.tareas.TareaCreateDto;
import com.example.tfgoposchool.dtos.tareas.TareaDto;
import com.example.tfgoposchool.dtos.tareas.TareaUpdateDto;
import com.example.tfgoposchool.exceptions.grupos.GrupoNotFound;
import com.example.tfgoposchool.exceptions.tareas.TareaNotFound;
import com.example.tfgoposchool.mappers.tareas.TareasMapper;
import com.example.tfgoposchool.models.Grupo;
import com.example.tfgoposchool.models.Tarea;
import com.example.tfgoposchool.repositories.grupos.GruposRepository;
import com.example.tfgoposchool.repositories.tareas.TareasRepository;
import com.example.tfgoposchool.storage.service.FileSystemStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class TareasServiceImpl implements TareasService{
    private final TareasRepository repository;
    private final TareasMapper mapper;
    private final GruposRepository gruposRepository;
    private final FileSystemStorageService storageService;

    @Autowired
    public TareasServiceImpl(TareasRepository repository, TareasMapper mapper, GruposRepository gruposRepository, FileSystemStorageService storageService) {
        this.repository = repository;
        this.mapper = mapper;
        this.gruposRepository = gruposRepository;
        this.storageService = storageService;
    }

    @Override
    public Page<TareaDto> findAll(Optional<String> titulo, Optional<String> descripcion,
                                  Optional<LocalDate> fechaEntrega, Optional<String> archivoUrl,
                                  Pageable pageable) {
        Specification<Tarea> specTitulo = (root, query, criteriaBuilder) ->
                titulo.map(t -> criteriaBuilder.like(criteriaBuilder.lower(root.get("titulo")), "%" + t + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Tarea> specDescripcion = (root, query, criteriaBuilder) ->
                descripcion.map(d -> criteriaBuilder.like(criteriaBuilder.lower(root.get("descripcion")), "%" + d + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Tarea> specArchivo = (root, query, criteriaBuilder) ->
                archivoUrl.map(a -> criteriaBuilder.like(criteriaBuilder.lower(root.get("archivoUrl")), "%" + a + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Tarea> specFechaEntrega = (root, query, criteriaBuilder) ->
                fechaEntrega.map(f -> criteriaBuilder.equal(root.get("fechaEntrega"), f))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Tarea> criterios = Specification.where(specTitulo).and(specDescripcion).and(specFechaEntrega).and(specArchivo);

        Page<TareaDto> lista = repository.findAll(criterios, pageable).map(mapper::toDto);

        return lista;
    }

    @Override
    public Page<TareaDto> findAllByGrupoId(Long grupoId, Pageable pageable) {
        Page<TareaDto> lista = repository.findByGrupoId(grupoId, pageable).map(mapper::toDto);

        return lista;
    }

    @Override
    public TareaDto findById(Long id) {
        Tarea existingTarea = repository.findById(id)
                .orElseThrow(() -> new TareaNotFound("La tarea con id " + id + " no existe"));

        return mapper.toDto(existingTarea);
    }

    @Override
    public TareaDto save(TareaCreateDto dto, Long grupoId, MultipartFile archivo) {
        Grupo existingGrupo = gruposRepository.findById(grupoId)
                .orElseThrow(() -> new GrupoNotFound("El grupo con id " + grupoId + " no existe"));

        String archivoUrl = addFile(null, archivo, true);

        Tarea tareaMapped = mapper.toEntity(dto, archivoUrl);
        tareaMapped.setGrupo(existingGrupo);
        existingGrupo.getTareas().add(tareaMapped);

        repository.save(tareaMapped);

        return mapper.toDto(tareaMapped);
    }

    @Override
    public TareaDto update(TareaUpdateDto dto, Long id, MultipartFile archivo) {
        Tarea existingTarea = repository.findById(id)
                .orElseThrow(() -> new TareaNotFound("La tarea con id " + id + " no existe"));

        String archivoUrl = addFile(id, archivo, true);

        Tarea tareaMapped = mapper.toEntity(dto, existingTarea, archivoUrl);

        repository.save(tareaMapped);

        return mapper.toDto(tareaMapped);
    }

    @Override
    public void deleteById(Long id) {
        Tarea existingTarea = repository.findById(id)
                .orElseThrow(() -> new TareaNotFound("La tarea con id " + id + " no existe"));

        repository.deleteById(id);
    }

    private String addFile(Long id, MultipartFile archivo, Boolean withUrl){
        if (id != null){
            Tarea existingTarea = repository.findById(id)
                    .orElseThrow(() -> new TareaNotFound("La tarea con id " + id + " no existe"));

            if (existingTarea.getArchivoUrl() != null){
                storageService.delete(existingTarea.getArchivoUrl());
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
