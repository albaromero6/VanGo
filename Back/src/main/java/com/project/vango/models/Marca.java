package com.project.vango.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "marca")
@Data
public class Marca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMar;

    @Column(length = 45, nullable = false, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "marca")
    @JsonManagedReference
    private List<Modelo> modelos;
}