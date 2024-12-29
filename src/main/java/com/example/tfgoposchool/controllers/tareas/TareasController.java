package com.example.tfgoposchool.controllers.tareas;

import com.example.tfgoposchool.dtos.alumnos.AlumnoDto;
import com.example.tfgoposchool.dtos.tareas.TareaCreateDto;
import com.example.tfgoposchool.dtos.tareas.TareaDto;
import com.example.tfgoposchool.dtos.tareas.TareaUpdateDto;
import com.example.tfgoposchool.services.tareas.TareasServiceImpl;
import com.example.tfgoposchool.utils.PaginationLinks;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/tareas")
public class TareasController {
    private final TareasServiceImpl service;
    private final PaginationLinks paginationLinks;

    @Autowired
    public TareasController(TareasServiceImpl service, PaginationLinks paginationLinks) {
        this.service = service;
        this.paginationLinks = paginationLinks;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
    @GetMapping("/")
    public ResponseEntity<Page<TareaDto>> findAll(
            @RequestParam() Optional<String> titulo,
            @RequestParam() Optional<String> descripcion,
            @RequestParam() Optional<LocalDate> fechaEntrega,
            @RequestParam() Optional<String> archivoUrl,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ){
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<TareaDto> pageResult = service.findAll(titulo, descripcion, fechaEntrega, archivoUrl, PageRequest.of(page, size, sort));

        return ResponseEntity.ok()
                .header("link", paginationLinks.createLinkHeader(pageResult, uri))
                .body(pageResult);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR', 'ALUMNO')")
    @GetMapping("/grupo/{id}")
    public ResponseEntity<Page<TareaDto>> findAllByGrupo(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "titulo") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ){
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<TareaDto> pageResult = service.findAllByGrupoId(id, PageRequest.of(page, size, sort));

        return ResponseEntity.ok()
                .header("link", paginationLinks.createLinkHeader(pageResult, uri))
                .body(pageResult);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TareaDto> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
    @PostMapping(value = "/{grupoId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TareaDto> save(@Validated @RequestPart("dto") TareaCreateDto dto,
                                         @PathVariable Long grupoId,
                                         @RequestPart("archivo")MultipartFile archivo){

        List<String> permittedContentTypes = List.of("application/pdf", "text/plain");

        String contentType = archivo.getContentType();

        if (contentType != null && !contentType.isEmpty() && permittedContentTypes.contains(contentType.toLowerCase())) {
            return ResponseEntity.ok(service.save(dto, grupoId, archivo));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha enviado un archivo valido");
        }

    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TareaDto> update(
            @Validated @RequestPart("dto") TareaUpdateDto dto,
            @PathVariable Long id,
            @RequestPart("archivo")MultipartFile archivo
    ){
        List<String> permittedContentTypes = List.of("application/pdf", "text/plain");

        String contentType = archivo.getContentType();

        if (contentType != null && !contentType.isEmpty() && permittedContentTypes.contains(contentType.toLowerCase())) {
            return ResponseEntity.ok(service.update(dto, id, archivo));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha enviado un archivo valido");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
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
