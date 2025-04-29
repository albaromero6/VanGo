package com.project.vango.models;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuario")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsu;

    @Column(length = 45, nullable = false)
    private String nombre;

    @Column(length = 45, nullable = false)
    private String apellido;

    @Column(length = 45, nullable = false, unique = true)
    private String email;

    @Column(length = 45, nullable = false)
    private String password;

    @Column(length = 15)
    private String telefono;

    private LocalDate registro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol = Rol.CLIENTE;

    public enum Rol {
        ADMINISTRADOR,
        CLIENTE
    }
}
