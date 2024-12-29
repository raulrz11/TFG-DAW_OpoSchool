package com.example.tfgoposchool.models.subscripciones;

import com.example.tfgoposchool.models.Alumno;
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
public class Subscripcion {
    public static Double PRECIO_SUBSCRIPCION = 50.00;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate fechaInicio;
    @Column(nullable = false)
    private LocalDate fechaFin;
    @Column(nullable = false)
    private Estado estado;
    @Column(nullable = false)
    @Builder.Default
    private Double precio = PRECIO_SUBSCRIPCION;
    @Column(nullable = false)
    @Embedded
    private Tarjeta tarjetaCredito;
    @Column
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    @Column
    private LocalDateTime deletedAt;
    @ManyToOne
    @JoinColumn(name = "alumno_id")
    private Alumno alumno;

    public static LocalDate getPrimerDiaMesSiguiente(){
        LocalDate fechaActual = LocalDate.now();
        return fechaActual.withDayOfMonth(1).plusMonths(1);
    }

    public enum Estado{
        ACTIVA, PENDIENTE, INACTIVA
    }
}
