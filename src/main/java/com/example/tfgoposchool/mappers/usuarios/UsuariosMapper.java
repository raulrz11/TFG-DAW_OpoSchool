package com.example.tfgoposchool.mappers.usuarios;

import com.example.tfgoposchool.dtos.usuarios.UsuarioDto;
import com.example.tfgoposchool.dtos.usuarios.UsuarioUpdateDto;
import com.example.tfgoposchool.models.Alumno;
import com.example.tfgoposchool.models.Profesor;
import com.example.tfgoposchool.models.usuarios.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class UsuariosMapper {
    private final PasswordEncoder passwordEncoder;
    public static String USER_PASSWORD = null;

    public UsuariosMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario newUsuarioFromAlumno(Alumno alumno){
        var rawPassword = UUID.randomUUID().toString();
        var primeraLetraNombre = alumno.getNombre().charAt(0);
        var primerApellido = alumno.getApellidos().split(" ")[0];
        var numeroRandom = (int) (Math.random() * 90) + 10;
        Usuario usuario = Usuario.builder()
                .nombre(alumno.getNombre())
                .username((primeraLetraNombre + primerApellido + numeroRandom).toLowerCase())
                .email(alumno.getEmail())
                .password(passwordEncoder.encode(rawPassword))
                .rols(Stream.of(Usuario.Rol.ALUMNO).collect(Collectors.toSet()))
                .build();

        USER_PASSWORD = rawPassword;

        System.out.println("EL username es: " + usuario.getUsername() + " y la password es: " + rawPassword);

        return usuario;
    }

    public Usuario newUsuarioFromProfesor(Profesor profesor){
        var primeraLetraNombre = profesor.getNombre().charAt(0);
        var primerApellido = profesor.getApellidos().split(" ")[0];
        var numeroRandom = (int) (Math.random() * 90) + 10;
        Usuario usuario = Usuario.builder()
                .nombre(profesor.getNombre())
                .username((primeraLetraNombre + primerApellido + numeroRandom).toLowerCase())
                .email(profesor.getEmail())
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .rols(Stream.of(Usuario.Rol.PROFESOR).collect(Collectors.toSet()))
                .build();

        return usuario;
    }

    public Usuario toEntity(UsuarioUpdateDto dto, Usuario existingUsuario){
        existingUsuario.setEmail(dto.getEmail() != null ? dto.getEmail() : existingUsuario.getEmail());
        existingUsuario.setPassword(dto.getPassword() != null ? passwordEncoder.encode(dto.getPassword()) : existingUsuario.getPassword());

        return existingUsuario;
    }

    public UsuarioDto toDto(Usuario usuario){
        UsuarioDto dto = UsuarioDto.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .rols(usuario.getRols().stream().map(Usuario.Rol::name).collect(Collectors.toSet()))
                .alumno(usuario.getAlumno() != null ? usuario.getAlumno().getNombre() + " " + usuario.getAlumno().getApellidos() : "")
                .profesor(usuario.getProfesor() != null ? usuario.getProfesor().getNombre() + " " + usuario.getProfesor().getApellidos() : "")
                .imagenPerfil(usuario.getImagenPerfil())
                .build();

        return dto;
    }
}
