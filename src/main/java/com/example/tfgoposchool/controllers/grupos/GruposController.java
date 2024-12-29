package com.example.tfgoposchool.controllers.grupos;

import com.example.tfgoposchool.dtos.alumnos.AlumnoDto;
import com.example.tfgoposchool.dtos.grupos.GrupoCreateDto;
import com.example.tfgoposchool.dtos.grupos.GrupoDto;
import com.example.tfgoposchool.dtos.grupos.GrupoUpdateDto;
import com.example.tfgoposchool.services.grupos.GruposServiceImpl;
import com.example.tfgoposchool.utils.PaginationLinks;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/grupos")
public class GruposController {
    private final GruposServiceImpl service;
    private final PaginationLinks paginationLinks;

    @Autowired
    public GruposController(GruposServiceImpl service, PaginationLinks paginationLinks) {
        this.service = service;
        this.paginationLinks = paginationLinks;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<Page<GrupoDto>> findAll(
            @RequestParam() Optional<String> nombre,
            @RequestParam()Optional<LocalDate> fechaClase,
            @RequestParam()Optional<LocalTime> horaClase,
            @RequestParam()Optional<Duration> duracionClase,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ){
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<GrupoDto> pageResult = service.findAll(nombre, fechaClase, horaClase, duracionClase, PageRequest.of(page, size, sort));

        return ResponseEntity.ok()
                .header("link", paginationLinks.createLinkHeader(pageResult, uri))
                .body(pageResult);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/alumnosInGrupo/{id}")
    public ResponseEntity<List<AlumnoDto>> findAlumnosInGrupo(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.findAlumnosInGrupo(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/alumnosInNoGrupo/{id}")
    public ResponseEntity<List<AlumnoDto>> findAlumnosInNoGrupo(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.findAlumnosInNoGrupo(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
    @GetMapping("/profesor/{id}")
    public ResponseEntity<Page<GrupoDto>> findAllByProfesor(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ){
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<GrupoDto> pageResult = service.findAllByProfesorId(id, PageRequest.of(page, size, sort));

        return ResponseEntity.ok()
                .header("link", paginationLinks.createLinkHeader(pageResult, uri))
                .body(pageResult);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALUMNO')")
    @GetMapping("/alumno/{alumnoId}")
    public ResponseEntity<GrupoDto> findByAlumnoId(@PathVariable Long alumnoId){
        return ResponseEntity.status(HttpStatus.OK).body(service.findByAlumnoId(alumnoId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<GrupoDto> save(@Validated @RequestBody GrupoCreateDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<GrupoDto> update(@Validated @RequestBody GrupoUpdateDto dto, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.update(dto, id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.delteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
    @PutMapping("/addAlumno/{alumnoId}/{grupoId}")
    public ResponseEntity<Void> addAlumno(@PathVariable Long alumnoId, @PathVariable Long grupoId){
        service.addAlumno(alumnoId, grupoId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
    @PutMapping("/removeAlumno/{alumnoId}")
    public ResponseEntity<Void> removeAlumno(@PathVariable Long alumnoId){
        service.removeAlumno(alumnoId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //metodo que permite mostrar los mensajes de error
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
