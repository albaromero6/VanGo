package com.project.vango.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import com.project.vango.validation.FechaActual;
import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "resenia")
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

    @FechaActual(message = "La fecha debe ser la fecha actual")
    @Column(nullable = false)
    private LocalDate fecha;

    @OneToOne
    @JoinColumn(name = "idReser", nullable = false, unique = true)
    private Reserva reserva;
}
