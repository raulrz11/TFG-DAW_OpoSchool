package com.example.tfgoposchool.controllers.profesores;

import com.example.tfgoposchool.dtos.alumnos.AlumnoCreateDto;
import com.example.tfgoposchool.dtos.alumnos.AlumnoDto;
import com.example.tfgoposchool.dtos.alumnos.AlumnoUpdateDto;
import com.example.tfgoposchool.dtos.profesores.ProfesorCreateDto;
import com.example.tfgoposchool.dtos.profesores.ProfesorDto;
import com.example.tfgoposchool.dtos.profesores.ProfesorUpdateDto;
import com.example.tfgoposchool.dtos.usuarios.UsuarioDto;
import com.example.tfgoposchool.services.profesores.ProfesoresServiceImpl;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/profesores")
public class ProfesoresController {
    private final ProfesoresServiceImpl service;
    private final PaginationLinks paginationLinks;

    @Autowired
    public ProfesoresController(ProfesoresServiceImpl service, PaginationLinks paginationLinks) {
        this.service = service;
        this.paginationLinks = paginationLinks;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<Page<ProfesorDto>> findAll(
            @RequestParam() Optional<String> nombre,
            @RequestParam()Optional<String> apellidos,
            @RequestParam()Optional<String> email,
            @RequestParam()Optional<String> telefono,
            @RequestParam()Optional<String> dni,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ){
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<ProfesorDto> pageResult = service.findAll(nombre, apellidos, email, telefono, dni, PageRequest.of(page, size, sort));

        return ResponseEntity.ok()
                .header("link", paginationLinks.createLinkHeader(pageResult, uri))
                .body(pageResult);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ProfesorDto> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALUMNO')")
    @GetMapping("/profesor/{nombre}")
    public ResponseEntity<ProfesorDto> findById(@PathVariable String nombre){
        return ResponseEntity.status(HttpStatus.OK).body(service.findByNombre(nombre));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<ProfesorDto> create(@Validated @RequestBody ProfesorCreateDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProfesorDto> update(@Validated @RequestBody ProfesorUpdateDto dto, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.update(dto, id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.deleteById(id);
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
