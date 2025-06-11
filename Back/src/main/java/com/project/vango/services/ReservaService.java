package com.project.vango.services;

import com.project.vango.models.Reserva;
import com.project.vango.models.Usuario;
import com.project.vango.repositories.ReservaRepository;
import com.project.vango.repositories.ReseniaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ReseniaRepository reseniaRepository;

    public List<Reserva> findAll() {
        try {
            logger.info("Obteniendo todas las reservas");
            List<Reserva> reservas = reservaRepository.findAll();
            logger.info("Reservas encontradas: {}", reservas.size());

            // Actualizar estados y cargar relaciones
            for (Reserva reserva : reservas) {
                try {
                    actualizarEstadoReserva(reserva);
                    // Asegurarse de que las relaciones estén cargadas
                    if (reserva.getUsuario() != null) {
                        logger.debug("Usuario cargado para reserva {}: {}", reserva.getIdReser(),
                                reserva.getUsuario().getEmail());
                    }
                    if (reserva.getVehiculo() != null && reserva.getVehiculo().getModelo() != null) {
                        logger.debug("Vehículo cargado para reserva {}: {}", reserva.getIdReser(),
                                reserva.getVehiculo().getIdVeh());
                        logger.debug("Modelo cargado para vehículo {}: {}", reserva.getVehiculo().getIdVeh(),
                                reserva.getVehiculo().getModelo().getNombre());
                        if (reserva.getVehiculo().getModelo().getMarca() != null) {
                            logger.debug("Marca cargada para modelo {}: {}",
                                    reserva.getVehiculo().getModelo().getIdMod(),
                                    reserva.getVehiculo().getModelo().getMarca().getNombre());
                        }
                    }
                    if (reserva.getIdSed_Salid() != null) {
                        logger.debug("Sede salida cargada para reserva {}: {}", reserva.getIdReser(),
                                reserva.getIdSed_Salid().getCiudad());
                    }
                    if (reserva.getIdSed_Lleg() != null) {
                        logger.debug("Sede llegada cargada para reserva {}: {}", reserva.getIdReser(),
                                reserva.getIdSed_Lleg().getCiudad());
                    }
                } catch (Exception e) {
                    logger.error("Error al procesar reserva {}: {}", reserva.getIdReser(), e.getMessage());
                }
            }
            return reservas;
        } catch (Exception e) {
            logger.error("Error al obtener todas las reservas: ", e);
            throw new RuntimeException("Error al obtener las reservas: " + e.getMessage());
        }
    }

    public Optional<Reserva> findById(Integer id) {
        try {
            Optional<Reserva> reservaOpt = reservaRepository.findById(id);
            if (reservaOpt.isPresent()) {
                Reserva reserva = reservaOpt.get();
                actualizarEstadoReserva(reserva);
                reservaRepository.save(reserva);
            }
            return reservaOpt;
        } catch (Exception e) {
            logger.error("Error al obtener la reserva con ID {}: ", id, e);
            throw new RuntimeException("Error al obtener la reserva: " + e.getMessage());
        }
    }

    public List<Reserva> findByUsuario(Usuario usuario) {
        return reservaRepository.findByUsuario(usuario);
    }

    public Reserva save(Reserva reserva) {
        actualizarEstadoReserva(reserva);
        return reservaRepository.save(reserva);
    }

    @Transactional
    public void deleteById(Integer id) {
        try {
            // Primero eliminamos las reseñas asociadas a la reserva
            reseniaRepository.deleteByReservaId(id);
            // Luego eliminamos la reserva
            reservaRepository.deleteById(id);
            logger.info("Reserva {} eliminada exitosamente", id);
        } catch (Exception e) {
            logger.error("Error al eliminar la reserva {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al eliminar la reserva: " + e.getMessage());
        }
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

    private void actualizarEstadoReserva(Reserva reserva) {
        try {
            LocalDate fechaActual = LocalDate.now();
            LocalDate fechaInicio = reserva.getInicio();
            LocalDate fechaFin = reserva.getFin();

            if (fechaActual.isAfter(fechaFin)) {
                reserva.setEstado(Reserva.Estado.FINALIZADA);
            } else if (fechaActual.isAfter(fechaInicio) || fechaActual.isEqual(fechaInicio)) {
                reserva.setEstado(Reserva.Estado.CURSO);
            } else {
                reserva.setEstado(Reserva.Estado.RESERVADA);
            }
            logger.debug("Estado actualizado para reserva {}: {}", reserva.getIdReser(), reserva.getEstado());
        } catch (Exception e) {
            logger.error("Error al actualizar el estado de la reserva {}: {}", reserva.getIdReser(), e.getMessage());
            throw new RuntimeException("Error al actualizar el estado de la reserva: " + e.getMessage());
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