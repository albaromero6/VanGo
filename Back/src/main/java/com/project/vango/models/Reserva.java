package com.project.vango.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "reserva")
@Data
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReser;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @Column(nullable = false)
    private Double precioTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.RESERVADA;

    @ManyToOne
    @JoinColumn(name = "idVeh", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name = "idUsu", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "idSed_Salid", nullable = false)
    private Sede sedeSalida;

    @ManyToOne
    @JoinColumn(name = "idSed_Lleg", nullable = false)
    private Sede sedeLlegada;

    public enum Estado {
        RESERVADA,
        EN_CURSO,
        FINALIZADA
    }
}
