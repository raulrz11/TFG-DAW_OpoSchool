package com.example.tfgoposchool.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Entrega {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @Builder.Default
    private EstadoEntrega estado = EstadoEntrega.PENDIENTE;
    @Column
    @Builder.Default
    private LocalDateTime fechaEntrega = LocalDateTime.now();
    @Column(nullable = false)
    private String archivoUrl;
    @Column
    private Double calificacion;
    @Column
    private String comentarios;
    @ManyToOne
    @JoinColumn(name = "alumno_id")
    private Alumno alumno;
    @ManyToOne
    @JoinColumn(name = "tarea_id")
    private Tarea tarea;

    public enum EstadoEntrega{
        PENDIENTE, CORREGIDA
    }
}
