package com.project.vango.repositories;

import com.project.vango.models.Reserva;
import com.project.vango.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    @Query("SELECT DISTINCT r FROM Reserva r " +
            "LEFT JOIN FETCH r.usuario " +
            "LEFT JOIN FETCH r.vehiculo v " +
            "LEFT JOIN FETCH v.modelo m " +
            "LEFT JOIN FETCH m.marca " +
            "LEFT JOIN FETCH r.idSed_Salid " +
            "LEFT JOIN FETCH r.idSed_Lleg")
    List<Reserva> findAll();

    @Query("SELECT r FROM Reserva r " +
            "LEFT JOIN FETCH r.usuario " +
            "LEFT JOIN FETCH r.vehiculo v " +
            "LEFT JOIN FETCH v.modelo m " +
            "LEFT JOIN FETCH m.marca " +
            "LEFT JOIN FETCH r.idSed_Salid " +
            "LEFT JOIN FETCH r.idSed_Lleg " +
            "WHERE r.usuario = :usuario")
    List<Reserva> findByUsuario(@Param("usuario") Usuario usuario);

    @Query("SELECT r FROM Reserva r " +
            "LEFT JOIN FETCH r.usuario " +
            "LEFT JOIN FETCH r.vehiculo v " +
            "LEFT JOIN FETCH v.modelo m " +
            "LEFT JOIN FETCH m.marca " +
            "LEFT JOIN FETCH r.idSed_Salid " +
            "LEFT JOIN FETCH r.idSed_Lleg " +
            "WHERE r.usuario.idUsu = :usuarioId")
    List<Reserva> findByUsuarioId(@Param("usuarioId") Integer usuarioId);

    @Modifying
    @Query("DELETE FROM Reserva r WHERE r.usuario.idUsu = :usuarioId")
    void deleteByUsuarioId(@Param("usuarioId") Integer usuarioId);

    List<Reserva> findByEstadoAndInicioLessThanEqual(Reserva.Estado estado, LocalDate fecha);

    List<Reserva> findByEstadoAndFinLessThan(Reserva.Estado estado, LocalDate fecha);
}