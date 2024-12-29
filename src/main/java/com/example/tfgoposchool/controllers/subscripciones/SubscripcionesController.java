package com.example.tfgoposchool.controllers.subscripciones;

import com.example.tfgoposchool.dtos.subscripciones.SubscripcionCreateDto;
import com.example.tfgoposchool.dtos.subscripciones.SubscripcionDto;
import com.example.tfgoposchool.models.subscripciones.Subscripcion;
import com.example.tfgoposchool.services.subscripciones.SubscripcionesServiceImpl;
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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/subscripciones")
public class SubscripcionesController {
    private final SubscripcionesServiceImpl service;
    private final PaginationLinks paginationLinks;

    @Autowired
    public SubscripcionesController(SubscripcionesServiceImpl service, PaginationLinks paginationLinks) {
        this.service = service;
        this.paginationLinks = paginationLinks;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<Page<SubscripcionDto>> findAll(
            @RequestParam() Optional<LocalDate> fechaInicio,
            @RequestParam()Optional<LocalDate> fechaFin,
            @RequestParam()Optional<Subscripcion.Estado> estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "estado") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ){
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<SubscripcionDto> pageResult = service.findAll(fechaInicio, fechaFin, estado, PageRequest.of(page, size, sort));

        return ResponseEntity.ok()
                .header("link", paginationLinks.createLinkHeader(pageResult, uri))
                .body(pageResult);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALUMNO')")
    @GetMapping("/alumno/{id}")
    public ResponseEntity<Page<SubscripcionDto>> findAllByAlumno(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "estado") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ){
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<SubscripcionDto> pageResult = service.findByAlumnoId(id, PageRequest.of(page, size, sort));

        return ResponseEntity.ok()
                .header("link", paginationLinks.createLinkHeader(pageResult, uri))
                .body(pageResult);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALUMNO')")
    @PostMapping("/{id}")
    public ResponseEntity<SubscripcionDto> renewSubscripcion(@Validated @RequestBody SubscripcionCreateDto dto, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.renewSubscription(dto, id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALUMNO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteById(@PathVariable Long id){
        service.softDelete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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
