package com.example.tfgoposchool.models;

import com.example.tfgoposchool.models.subscripciones.Subscripcion;
import com.example.tfgoposchool.models.usuarios.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellidos;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String telefono;
    @Column(nullable = false, unique = true)
    private String dni;
    @Column
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    @Column
    @Builder.Default
    private Boolean isDeleted = false;
    @ManyToOne
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;
    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL)
    private List<Entrega> entregas;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;
    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL)
    private List<Subscripcion> subscripciones;
}
