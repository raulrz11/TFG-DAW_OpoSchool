package com.example.tfgoposchool.dtos.usuarios;

import com.example.tfgoposchool.models.Alumno;
import com.example.tfgoposchool.models.usuarios.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDto {
    Long id;
    String nombre;
    String username;
    String email;
    Set<String> rols;
    String alumno;
    String profesor;
    String imagenPerfil;
}
