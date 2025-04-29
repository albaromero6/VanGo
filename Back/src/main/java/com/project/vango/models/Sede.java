package com.project.vango.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sede")
@Data
public class Sede {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSed;

    @Column(length = 150, nullable = false)
    private String direccion;

    @Column(length = 45, nullable = false)
    private String ciudad;

    @Column(length = 15)
    private String telefono;
}
