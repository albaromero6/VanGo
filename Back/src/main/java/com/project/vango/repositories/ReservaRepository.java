package com.project.vango.repositories;

import com.project.vango.models.Reserva;
import com.project.vango.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    List<Reserva> findByUsuario(Usuario usuario);

    @Query("SELECT r FROM Reserva r WHERE r.usuario.idUsu = :usuarioId")
    List<Reserva> findByUsuarioId(@Param("usuarioId") Integer usuarioId);

    @Modifying
    @Query("DELETE FROM Reserva r WHERE r.usuario.idUsu = :usuarioId")
    void deleteByUsuarioId(@Param("usuarioId") Integer usuarioId);
}