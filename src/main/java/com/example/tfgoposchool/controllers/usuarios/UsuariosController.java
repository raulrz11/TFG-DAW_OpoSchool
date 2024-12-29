package com.example.tfgoposchool.controllers.usuarios;

import com.example.tfgoposchool.dtos.usuarios.UsuarioDto;
import com.example.tfgoposchool.dtos.usuarios.UsuarioUpdateDto;
import com.example.tfgoposchool.models.usuarios.Usuario;
import com.example.tfgoposchool.services.usuarios.UsuariosServiceImpl;
import com.example.tfgoposchool.utils.PaginationLinks;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuariosController {
    private final UsuariosServiceImpl service;
    private final PaginationLinks paginationLinks;

    @Autowired
    public UsuariosController(UsuariosServiceImpl service, PaginationLinks paginationLinks) {
        this.service = service;
        this.paginationLinks = paginationLinks;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<Page<UsuarioDto>> findAll(
            @RequestParam() Optional<String> nombre,
            @RequestParam()Optional<String> username,
            @RequestParam()Optional<String> email,
            @RequestParam()Optional<String> password,
            @RequestParam()Optional<Usuario.Rol> rol,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ){
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<UsuarioDto> pageResult = service.findAll(nombre, username, email, password, rol, PageRequest.of(page, size, sort));

        return ResponseEntity.ok()
                .header("link", paginationLinks.createLinkHeader(pageResult, uri))
                .body(pageResult);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALUMNO')")
    @PutMapping("/email/{id}")
    public ResponseEntity<UsuarioDto> updateEmail(@Validated @RequestBody UsuarioUpdateDto dto, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.updateEmail(dto, id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ALUMNO')")
    @PutMapping("/password/{id}")
    public ResponseEntity<UsuarioDto> updatePassword(@Validated @RequestBody UsuarioUpdateDto dto, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.updateEmail(dto, id));
    }

    //CURRENT USER
    @GetMapping("/current-user")
    public ResponseEntity<UsuarioDto> getCurrentUser(@AuthenticationPrincipal Usuario usuario){
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(usuario.getId()));
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
