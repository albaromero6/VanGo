package com.project.vango.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "marca")
public class Marca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMar;

    @Column(length = 45, nullable = false, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnore
    private List<Modelo> modelos;

    @PreRemove
    private void preRemove() {
        if (modelos != null) {
            modelos.forEach(modelo -> {
                if (modelo.getVehiculos() != null) {
                    modelo.getVehiculos().clear();
                }
            });
        }
    }
}