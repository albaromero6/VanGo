package com.project.vango.repositories;

import com.project.vango.models.Reserva;
import com.project.vango.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    List<Reserva> findByUsuario(Usuario usuario);
}