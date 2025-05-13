package com.project.vango.controllers;

import com.project.vango.models.Reserva;
import com.project.vango.models.Usuario;
import com.project.vango.services.ReservaService;
import com.project.vango.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private UsuarioService usuarioService;

    // Endpoints de administrador
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Reserva>> getAllReservas() {
        return ResponseEntity.ok(reservaService.findAll());
    }

    // Endpoints de cliente
    @GetMapping("/cliente/mis-reservas")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<Reserva>> getMisReservas() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(reservaService.findByUsuario(usuario));
    }

    // Endpoint compartido
    @GetMapping("/{id}")
    public ResponseEntity<? extends Object> getReservaById(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return reservaService.findById(id)
                .map(reserva -> {
                    // Verificar si el usuario es admin o el propietario de la reserva
                    if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ||
                            reserva.getUsuario().getIdUsu().equals(usuario.getIdUsu())) {
                        return ResponseEntity.ok(reserva);
                    }
                    return ResponseEntity.status(403).build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Reserva> createReserva(@RequestBody Reserva reserva) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        reserva.setUsuario(usuario);
        return ResponseEntity.ok(reservaService.save(reserva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<? extends Object> updateReserva(@PathVariable Integer id, @RequestBody Reserva reserva) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return reservaService.findById(id)
                .map(existingReserva -> {
                    // Verificar si el usuario es admin o el propietario de la reserva
                    if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ||
                            existingReserva.getUsuario().getIdUsu().equals(usuario.getIdUsu())) {
                        reserva.setIdReser(id);
                        reserva.setUsuario(existingReserva.getUsuario()); // Mantener el usuario original
                        return ResponseEntity.ok(reservaService.save(reserva));
                    }
                    return ResponseEntity.status(403).build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<? extends Object> deleteReserva(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return reservaService.findById(id)
                .map(reserva -> {
                    // Verificar si el usuario es admin o el propietario de la reserva
                    if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ||
                            reserva.getUsuario().getIdUsu().equals(usuario.getIdUsu())) {
                        reservaService.deleteById(id);
                        return ResponseEntity.ok().<Void>build();
                    }
                    return ResponseEntity.status(403).build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Reserva> updateEstado(
            @PathVariable Integer id,
            @RequestParam Reserva.Estado estado) {
        return reservaService.findById(id)
                .map(reserva -> {
                    reserva.setEstado(estado);
                    return ResponseEntity.ok(reservaService.save(reserva));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}