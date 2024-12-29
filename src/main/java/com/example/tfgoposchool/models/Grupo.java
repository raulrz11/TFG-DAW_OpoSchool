package com.example.tfgoposchool.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String nombre;
    @Column(nullable = false)
    private LocalDate fechaClase;
    @Column(nullable = false)
    private LocalTime horaClase;
    @Column(nullable = false)
    private String duracionClaseString;
    @Transient
    private Duration duracionClase;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "profesor_id")
    private Profesor profesor;
    @OneToMany(mappedBy = "grupo", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Alumno> alumnos;
    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL)
    private List<Tarea> tareas;

    //metodos para pasar de Duration a String y viceversa automaticamente
    @PrePersist
    @PreUpdate
    private void convertDurationToString() {
        this.duracionClaseString = duracionClase != null ? duracionClase.toString() : null;
    }

    @PostLoad
    private void convertStringToDuration() {
        this.duracionClase = duracionClaseString != null ? Duration.parse(duracionClaseString) : null;
    }
}
