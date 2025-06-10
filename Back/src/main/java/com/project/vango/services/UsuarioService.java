package com.project.vango.services;

import com.project.vango.models.Usuario;
import com.project.vango.models.Reserva;
import com.project.vango.repositories.UsuarioRepository;
import com.project.vango.repositories.ReservaRepository;
import com.project.vango.repositories.ReseniaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ReseniaRepository reseniaRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void deleteById(Integer id) {
        usuarioRepository.deleteById(id);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Transactional
    public void deleteUsuarioConReservas(Integer id) {
        Usuario usuario = findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Primero eliminamos todas las reseñas asociadas a las reservas del usuario
        List<Reserva> reservas = reservaRepository.findByUsuarioId(id);
        for (Reserva reserva : reservas) {
            reseniaRepository.deleteByReservaId(reserva.getIdReser());
        }

        // Luego eliminamos todas las reservas
        reservaRepository.deleteByUsuarioId(id);

        // Finalmente eliminamos el usuario
        usuarioRepository.deleteById(id);
    }

    public long countByRol(Usuario.Rol rol) {
        return usuarioRepository.countByRol(rol);
    }
}