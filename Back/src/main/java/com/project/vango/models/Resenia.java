package com.project.vango.models;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import java.time.LocalDate;

@Entity
@Table(name = "resenia")
@Data
public class Resenia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idResen;

    @Column(length = 255)
    private String comentario;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer puntuacion;

    private LocalDate fecha;

    @OneToOne
    @JoinColumn(name = "idReser", nullable = false, unique = true)
    private Reserva reserva;
}
