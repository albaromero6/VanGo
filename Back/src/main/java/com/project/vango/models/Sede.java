package com.project.vango.models;

import jakarta.persistence.*;
import com.project.vango.validation.Telefono;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sede")
public class Sede {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSed;

    @Column(length = 150, nullable = false)
    private String direccion;

    @Column(length = 45, nullable = false)
    private String ciudad;

    @Telefono(message = "El formato del teléfono no es válido")
    @Column(length = 15)
    private String telefono;

    @Column(length = 255)
    private String imagen;
}
