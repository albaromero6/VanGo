package com.project.vango.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehiculo")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVeh;

    @ManyToOne
    @JoinColumn(name = "idMod", nullable = false)
    private Modelo modelo;

    @Column(length = 255, nullable = false)
    private String imagen;

    @Min(value = 0, message = "El precio debe ser un número positivo")
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double precio;

    @Min(value = 2000, message = "El año debe ser 2000 o posterior")
    @Column(nullable = false)
    private Integer anio;

    @Min(value = 1, message = "El número de pasajeros debe ser al menos 1")
    @Max(value = 6, message = "El número de pasajeros no puede ser mayor a 6")
    @Column(nullable = false)
    private Integer pasajeros;

    @Min(value = 3, message = "El número de puertas debe ser al menos 3")
    @Max(value = 5, message = "El número de puertas no puede ser mayor a 5")
    @Column(nullable = false)
    private Integer puertas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Transmision transmision;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Combustible combustible;

    @Column(length = 255, nullable = false)
    private String detalles1;

    @Column(length = 255, nullable = false)
    private String detalles2;

    @Column(length = 255, nullable = false)
    private String detalles3;

    @Column(length = 255, nullable = false)
    private String detalles4;

    public enum Transmision {
        MANUAL,
        AUTOMATICO
    }

    public enum Combustible {
        GASOLINA,
        DIESEL
    }
}
