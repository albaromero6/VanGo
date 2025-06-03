package com.project.vango.models;

import java.time.LocalDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.vango.validation.DNI;
import com.project.vango.validation.Telefono;
import com.project.vango.validation.Password;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsu;

    @Column(length = 45, nullable = false)
    private String nombre;

    @Column(length = 45, nullable = false)
    private String apellido;

    @Email(message = "El formato del email no es válido")
    @Column(length = 45, nullable = false, unique = true)
    private String email;

    @DNI(message = "El DNI no es válido")
    @Column(length = 9, nullable = false, unique = true)
    private String dni;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(length = 255, nullable = false)
    @Password
    private String password;

    @Telefono(message = "El formato del teléfono no es válido")
    @Column(length = 15)
    private String telefono;

    @Column(length = 255, nullable = true)
    private String direccion;

    @Column(nullable = true)
    private LocalDate fechaNacimiento;

    private LocalDate registro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Rol rol = Rol.CLIENTE;

    public enum Rol {
        ADMINISTRADOR,
        CLIENTE
    }
}
