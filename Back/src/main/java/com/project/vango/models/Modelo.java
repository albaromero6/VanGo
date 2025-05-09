package com.project.vango.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "modelo", uniqueConstraints = @UniqueConstraint(columnNames = { "nombre", "idMar" }))
public class Modelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMod;

    @Column(length = 45, nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "idMar", nullable = false)
    @JsonBackReference
    private Marca marca;

    @OneToMany(mappedBy = "modelo", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Vehiculo> vehiculos;

    @PreRemove
    private void preRemove() {
        if (vehiculos != null) {
            vehiculos.forEach(vehiculo -> {
                vehiculo.setModelo(null);
            });
            vehiculos.clear();
        }
    }
}