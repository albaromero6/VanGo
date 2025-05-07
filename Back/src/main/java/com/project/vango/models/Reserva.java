package com.project.vango.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import com.project.vango.validation.FechasReserva;
import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reserva")
@FechasReserva(message = "Las fechas de la reserva no son válidas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReser;

    @Column(nullable = false)
    private LocalDate inicio;

    @Column(nullable = false)
    private LocalDate fin;

    @Min(value = 0, message = "El total debe ser un número positivo")
    @Column(nullable = false)
    private Double total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
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
        CURSO,
        FINALIZADA,
        CANCELADA
    }
}
