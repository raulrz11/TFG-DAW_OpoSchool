package com.example.tfgoposchool.controllers.entregas;

import com.example.tfgoposchool.dtos.entregas.EntregaDto;
import com.example.tfgoposchool.dtos.entregas.EntregaUpdateDto;
import com.example.tfgoposchool.models.Entrega;
import com.example.tfgoposchool.services.entregas.EntregasServiceImpl;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/entregas")
public class EntregasController {
    private final EntregasServiceImpl service;
    private final PaginationLinks paginationLinks;

    @Autowired
    public EntregasController(EntregasServiceImpl service, PaginationLinks paginationLinks) {
        this.service = service;
        this.paginationLinks = paginationLinks;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
    @GetMapping("/tarea/{idTarea}")
    public ResponseEntity<Page<EntregaDto>> findAllByTarea(
            @RequestParam() Optional<Entrega.EstadoEntrega> estado,
            @RequestParam() Optional<Double> calificacion,
            @RequestParam() Optional<LocalDateTime> fechaEntrega,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @PathVariable Long idTarea,
            HttpServletRequest request
    ){
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<EntregaDto> pageResult = service.findAllByTareaId(estado, calificacion, fechaEntrega, idTarea, PageRequest.of(page, size, sort));

        return ResponseEntity.ok()
                .header("link", paginationLinks.createLinkHeader(pageResult, uri))
                .body(pageResult);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALUMNO')")
    @GetMapping("/{id}")
    public ResponseEntity<EntregaDto> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALUMNO')")
    @GetMapping("/tarea/{tareaId}/alumno/{alumnoId}")
    public ResponseEntity<EntregaDto> findByTareaAndAlumno(@PathVariable Long tareaId, @PathVariable Long alumnoId){
        return ResponseEntity.status(HttpStatus.OK).body(service.findByTareaAndAlumno(tareaId, alumnoId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALUMNO')")
    @PostMapping(value = "/{tareaId}/{alumnoId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EntregaDto> save(@PathVariable Long tareaId,
                                           @PathVariable Long alumnoId,
                                           @RequestPart MultipartFile archivo){
        List<String> permittedContentTypes = List.of("application/pdf", "text/plain");

        String contentType = archivo.getContentType();

        if (!archivo.isEmpty() && contentType != null && !contentType.isEmpty() && permittedContentTypes.contains(contentType.toLowerCase())) {
            return ResponseEntity.ok(service.save(tareaId, alumnoId, archivo));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha enviado una imagen para el producto válida o esta está vacía");
        }

    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALUMNO')")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EntregaDto> update(@PathVariable Long id,
                                             @RequestPart("archivo") MultipartFile archivo){

        List<String> permittedContentTypes = List.of("application/pdf", "text/plain");

        String contentType = archivo.getContentType();

        if (!archivo.isEmpty() && contentType != null && !contentType.isEmpty() && permittedContentTypes.contains(contentType.toLowerCase())) {
            return ResponseEntity.ok(service.update(id, archivo));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha enviado una imagen para el producto válida o esta está vacía");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
    @PutMapping("/{id}")
    public ResponseEntity<EntregaDto> correctEntrega(@Validated @RequestBody EntregaUpdateDto dto, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.correctEntrega(dto, id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByIb(@PathVariable Long id){
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
