package com.project.vango.repositories;

import com.project.vango.models.Resenia;
import com.project.vango.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReseniaRepository extends JpaRepository<Resenia, Integer> {
    List<Resenia> findByReserva_Usuario(Usuario usuario);
}