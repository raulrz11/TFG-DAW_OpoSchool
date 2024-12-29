package com.example.tfgoposchool.services.subscripciones;

import com.example.tfgoposchool.dtos.subscripciones.SubscripcionCreateDto;
import com.example.tfgoposchool.dtos.subscripciones.SubscripcionDto;
import com.example.tfgoposchool.exceptions.AlumnoNotFound;
import com.example.tfgoposchool.exceptions.subscripciones.SubscripcionBadRequest;
import com.example.tfgoposchool.exceptions.subscripciones.SubscripcionNotFound;
import com.example.tfgoposchool.mappers.subscripciones.SubscripcionesMapper;
import com.example.tfgoposchool.models.Alumno;
import com.example.tfgoposchool.models.subscripciones.Subscripcion;
import com.example.tfgoposchool.repositories.alumnos.AlumnosRepository;
import com.example.tfgoposchool.repositories.subscripciones.SubscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SubscripcionesServiceImpl implements SubscripcionesService{
    private final SubscripcionRepository repository;
    private final SubscripcionesMapper mapper;
    private final AlumnosRepository alumnosRepository;

    @Autowired
    public SubscripcionesServiceImpl(SubscripcionRepository repository, SubscripcionesMapper mapper, AlumnosRepository alumnosRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.alumnosRepository = alumnosRepository;
    }

    @Override
    public Page<SubscripcionDto> findAll(Optional<LocalDate> fechaInicio, Optional<LocalDate> fechaFin,
                                         Optional<Subscripcion.Estado> estado, Pageable pageable) {

        Specification<Subscripcion> specFechaInicio = (root, query, criteriaBuilder) ->
                fechaInicio.map(fi -> criteriaBuilder.equal(root.get("fechaInicio"), fi))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Subscripcion> specFechaFin = (root, query, criteriaBuilder) ->
                fechaFin.map(ff -> criteriaBuilder.equal(root.get("fechaFin"), ff))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Subscripcion> specEstado = (root, query, criteriaBuilder) ->
                estado.map(e -> criteriaBuilder.equal(root.get("estado"), e))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Subscripcion> criterios = Specification.where(specFechaInicio)
                .and(specFechaFin).and(specEstado);

        Page<SubscripcionDto> lista = repository.findAll(criterios, pageable).map(mapper::toDto);

        return lista;
    }

    @Override
    public Page<SubscripcionDto> findByAlumnoId(Long id, Pageable pageable) {

        Page<SubscripcionDto> lista = repository.findByAlumnoId(id, pageable).map(mapper::toDto);

        return lista;
    }

    @Override
    public SubscripcionDto renewSubscription(SubscripcionCreateDto dto, Long idAlumno) {
        Alumno existingAlumno = alumnosRepository.findById(idAlumno)
                .orElseThrow(() -> new AlumnoNotFound("El alumno con id " + idAlumno + " no existe"));

        boolean estadoSubscripcion = existingAlumno.getSubscripciones().stream()
                .anyMatch(s -> s.getEstado() == Subscripcion.Estado.ACTIVA);

        if (!estadoSubscripcion){
            Subscripcion subscripcionMapped = mapper.toEntity(dto);

            subscripcionMapped.setAlumno(existingAlumno);
            existingAlumno.getSubscripciones().add(subscripcionMapped);

            alumnosRepository.save(existingAlumno);

            return mapper.toDto(subscripcionMapped);
        }else {
            throw new SubscripcionNotFound("El alumno " + existingAlumno.getNombre() + " ya tiene una subscripcion activa");
        }
    }

    @Override
    public void activateSubscription(Subscripcion existingSubscripcion){
        existingSubscripcion.setEstado(Subscripcion.Estado.ACTIVA);
        existingSubscripcion.setUpdatedAt(LocalDateTime.now());
        repository.save(existingSubscripcion);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    //@Scheduled(cron = "*/10 * * * * ?")
    public void activateAllSubscriptions(){
        LocalDate fechaActual = LocalDate.now();
        //LocalDate fechaActual = LocalDate.of(2025, 01, 01);

        List<Subscripcion> subscripcionesToActive = repository.findByFechaInicioAndEstado(fechaActual, Subscripcion.Estado.PENDIENTE);

        if (!subscripcionesToActive.isEmpty()){
            try {
                for (Subscripcion subscripcion : subscripcionesToActive){
                    System.out.println(subscripcion.getAlumno().getNombre());
                    activateSubscription(subscripcion);
                }
                System.out.println("Todas las subscripciones de hoy activadas con exito");
            } catch (Exception e) {
                throw new SubscripcionBadRequest("Error al activar la subscripcion");
            }
        }else {
            System.out.println("No hay suscripciones para activar");
        }
    }

    @Override
    public void automaticRenewSubscription(Subscripcion existingSubscripcion) {
        existingSubscripcion.setFechaInicio(Subscripcion.getPrimerDiaMesSiguiente());
        existingSubscripcion.setFechaFin(Subscripcion.getPrimerDiaMesSiguiente().plusDays(30));
        existingSubscripcion.setUpdatedAt(LocalDateTime.now());
        repository.save(existingSubscripcion);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    //@Scheduled(cron = "*/10 * * * * ?")
    public void automaticRenewAllSubscriptions(){
        LocalDate fechaActual = LocalDate.now();
        //LocalDate fechaActual = LocalDate.of(2024, 12, 30);

        List<Subscripcion> subscripcionesToRenew = repository.findByFechaFin(fechaActual);

        if (!subscripcionesToRenew.isEmpty()){
            try {
                for (Subscripcion subscripcion : subscripcionesToRenew){
                    System.out.println(subscripcion.getAlumno().getNombre());
                    automaticRenewSubscription(subscripcion);
                }
                System.out.println("Todas las subscripciones de hoy renovadas con exito");
            } catch (Exception e) {
                throw new SubscripcionBadRequest("Error al renovar la subscripcion");
            }
        }else {
            System.out.println("No hay suscripciones para renovar");
        }
    }

    @Override
    public void softDelete(Long id) {
        Subscripcion existingSubscripcion = repository.findById(id)
                .orElseThrow(() -> new SubscripcionBadRequest("La subscripcion con id " + id + " no existe"));
        var existingAlumno = existingSubscripcion.getAlumno();

        existingAlumno.setIsDeleted(true);
        existingAlumno.getUsuario().setIsActive(false);

        existingSubscripcion.setDeletedAt(LocalDateTime.now());
        existingSubscripcion.setEstado(Subscripcion.Estado.INACTIVA);

        alumnosRepository.save(existingAlumno);
    }
}
