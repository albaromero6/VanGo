package com.project.vango.controllers;

import com.project.vango.models.Resenia;
import com.project.vango.models.Usuario;
import com.project.vango.services.ReseniaService;
import com.project.vango.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/resenias")
@CrossOrigin(origins = "*")
public class ReseniaController {

    @Autowired
    private ReseniaService reseniaService;

    @Autowired
    private UsuarioService usuarioService;

    // Endpoints de administrador
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Resenia>> getAllResenias() {
        return ResponseEntity.ok(reseniaService.findAll());
    }

    // Endpoints de cliente
    @GetMapping("/cliente/mis-resenias")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<Resenia>> getMisResenias() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(reseniaService.findByUsuario(usuario));
    }

    // Endpoint compartido
    @GetMapping("/{id}")
    public ResponseEntity<? extends Object> getReseniaById(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return reseniaService.findById(id)
                .map(resenia -> {
                    // Verificar si el usuario es admin o el propietario de la reseña
                    if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ||
                            resenia.getReserva().getUsuario().getIdUsu().equals(usuario.getIdUsu())) {
                        return ResponseEntity.ok(resenia);
                    }
                    return ResponseEntity.status(403).build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Resenia>> getReseniasByUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(reseniaService.findAll());
    }

    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<Resenia>> getReseniasByVehiculo(@PathVariable Integer vehiculoId) {
        return ResponseEntity.ok(reseniaService.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Resenia> createResenia(@RequestBody Resenia resenia) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar que la reserva pertenece al usuario
        if (!resenia.getReserva().getUsuario().getIdUsu().equals(usuario.getIdUsu())) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(reseniaService.save(resenia));
    }

    @PutMapping("/{id}")
    public ResponseEntity<? extends Object> updateResenia(@PathVariable Integer id, @RequestBody Resenia resenia) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return reseniaService.findById(id)
                .map(existingResenia -> {
                    // Verificar si el usuario es admin o el propietario de la reseña
                    if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ||
                            existingResenia.getReserva().getUsuario().getIdUsu().equals(usuario.getIdUsu())) {
                        resenia.setIdResen(id);
                        resenia.setReserva(existingResenia.getReserva()); // Mantener la reserva original
                        return ResponseEntity.ok(reseniaService.save(resenia));
                    }
                    return ResponseEntity.status(403).build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<? extends Object> deleteResenia(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return reseniaService.findById(id)
                .map(resenia -> { 
                    // Verificar si el usuario es admin o el propietario de la reseña
                    if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ||
                            resenia.getReserva().getUsuario().getIdUsu().equals(usuario.getIdUsu())) {
                        reseniaService.deleteById(id);
                        return ResponseEntity.ok().<Void>build();
                    }
                    return ResponseEntity.status(403).build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}