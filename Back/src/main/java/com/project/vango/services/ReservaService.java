package com.project.vango.services;

import com.project.vango.models.Reserva;
import com.project.vango.models.Usuario;
import com.project.vango.repositories.ReservaRepository;
import com.project.vango.repositories.ReseniaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ReseniaRepository reseniaRepository;

    public List<Reserva> findAll() {
        List<Reserva> reservas = reservaRepository.findAll();
        actualizarEstadosReservas(reservas);
        return reservas;
    }

    public Optional<Reserva> findById(Integer id) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(id);
        if (reservaOpt.isPresent()) {
            Reserva reserva = reservaOpt.get();
            actualizarEstadoReserva(reserva);
            reservaRepository.save(reserva);
        }
        return reservaOpt;
    }

    public List<Reserva> findByUsuario(Usuario usuario) {
        List<Reserva> reservas = reservaRepository.findByUsuario(usuario);
        actualizarEstadosReservas(reservas);
        return reservas;
    }

    public Reserva save(Reserva reserva) {
        actualizarEstadoReserva(reserva);
        return reservaRepository.save(reserva);
    }

    public void deleteById(Integer id) {
        reservaRepository.deleteById(id);
    }

    @Transactional
    public void deleteByUsuarioId(Integer usuarioId) {
        try {
            List<Reserva> reservas = reservaRepository.findByUsuarioId(usuarioId);
            for (Reserva reserva : reservas) {
                try {
                    // Primero eliminamos las reseñas asociadas a la reserva
                    reseniaRepository.deleteByReservaId(reserva.getIdReser());
                    // Luego eliminamos la reserva
                    reservaRepository.delete(reserva);
                } catch (Exception e) {
                    throw new RuntimeException(
                            "Error al eliminar la reserva " + reserva.getIdReser() + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al eliminar las reservas del usuario " + usuarioId + ": " + e.getMessage());
        }
    }

    private void actualizarEstadosReservas(List<Reserva> reservas) {
        boolean hayCambios = false;
        for (Reserva reserva : reservas) {
            Reserva.Estado estadoAnterior = reserva.getEstado();
            actualizarEstadoReserva(reserva);
            if (estadoAnterior != reserva.getEstado()) {
                hayCambios = true;
            }
        }
        if (hayCambios) {
            reservaRepository.saveAll(reservas);
        }
    }

    private void actualizarEstadoReserva(Reserva reserva) {
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaInicio = reserva.getInicio();
        LocalDate fechaFin = reserva.getFin();

        if (fechaActual.isAfter(fechaFin)) {
            // Si la fecha actual es posterior a la fecha de fin, la reserva está finalizada
            reserva.setEstado(Reserva.Estado.FINALIZADA);
        } else if (fechaActual.isAfter(fechaInicio) || fechaActual.isEqual(fechaInicio)) {
            // Si la fecha actual está entre la fecha de inicio y fin, la reserva está en curso
            reserva.setEstado(Reserva.Estado.CURSO);
        } else {
            // Si la fecha actual es anterior a la fecha de inicio, la reserva está reservada
            reserva.setEstado(Reserva.Estado.RESERVADA);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?") // Se ejecuta todos los días a medianoche
    @Transactional
    public void actualizarEstadosReservas() {
        LocalDate hoy = LocalDate.now();

        // Actualizar reservas que deben pasar a EN CURSO
        List<Reserva> reservasParaIniciar = reservaRepository.findByEstadoAndInicioLessThanEqual(
                Reserva.Estado.RESERVADA, hoy);
        for (Reserva reserva : reservasParaIniciar) {
            reserva.setEstado(Reserva.Estado.CURSO);
            reservaRepository.save(reserva);
        }

        // Actualizar reservas que deben pasar a FINALIZADA
        List<Reserva> reservasParaFinalizar = reservaRepository.findByEstadoAndFinLessThan(
                Reserva.Estado.CURSO, hoy);
        for (Reserva reserva : reservasParaFinalizar) {
            reserva.setEstado(Reserva.Estado.FINALIZADA);
            reservaRepository.save(reserva);
        }
    }
}