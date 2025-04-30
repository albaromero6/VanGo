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

    private LocalDate inicio;

    private LocalDate fin;

    @Column(nullable = false)
    private Double total;

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
    private Sede idSed_Salid;

    @ManyToOne
    @JoinColumn(name = "idSed_Lleg", nullable = false)
    private Sede idSed_Lleg;

    public enum Estado {
        RESERVADA,
        EN_CURSO,
        FINALIZADA
    }
}
