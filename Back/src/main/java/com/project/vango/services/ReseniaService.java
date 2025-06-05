package com.project.vango.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.vango.models.Resenia;
import com.project.vango.models.Usuario;
import com.project.vango.models.Reserva;
import com.project.vango.repositories.ReseniaRepository;
import java.util.List;
import java.util.Optional;

@Service
public class ReseniaService {

    @Autowired
    private ReseniaRepository reseniaRepository;

    public List<Resenia> findAll() {
        return reseniaRepository.findAll();
    }

    public Optional<Resenia> findById(Integer id) {
        return reseniaRepository.findById(id);
    }

    public List<Resenia> findByUsuario(Usuario usuario) {
        return reseniaRepository.findByReserva_Usuario(usuario);
    }

    public boolean existsByReserva(Reserva reserva) {
        return reseniaRepository.existsByReserva(reserva);
    }

    public Resenia save(Resenia resenia) {
        return reseniaRepository.save(resenia);
    }

    public void deleteById(Integer id) {
        reseniaRepository.deleteById(id);
    }

    public Optional<Resenia> findByReserva(Reserva reserva) {
        return reseniaRepository.findByReserva(reserva);
    }

    public List<Resenia> findByVehiculoId(Integer vehiculoId) {
        return reseniaRepository.findByReservaVehiculoIdVeh(vehiculoId);
    }
}