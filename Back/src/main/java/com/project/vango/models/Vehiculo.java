package com.project.vango.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vehiculo")
@Data
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVeh;

    @Column(length = 45, nullable = false)
    private String marca;

    @Column(length = 45, nullable = false)
    private String modelo;

    @Column(length = 45, nullable = false, unique = true)
    private String matricula;

    @Column(length = 150)
    private String descripcion;

    @Column(length = 150)
    private String imagen;

    private Double precio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Disponibilidad disponibilidad = Disponibilidad.DISPONIBLE;

    public enum Disponibilidad {
        DISPONIBLE,
        INDISPONIBLE
    }
}
