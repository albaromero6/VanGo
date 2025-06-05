package com.project.vango.controllers;

import com.project.vango.models.Resenia;
import com.project.vango.models.Usuario;
import com.project.vango.models.Reserva;
import com.project.vango.services.ReseniaService;
import com.project.vango.services.UsuarioService;
import com.project.vango.services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/resenias")
@CrossOrigin(origins = "*")
@Tag(name = "Reseñas", description = "API para la gestión de reseñas de vehículos")
@SecurityRequirement(name = "bearerAuth")
public class ReseniaController {

        @Autowired
        private ReseniaService reseniaService;

        @Autowired
        private UsuarioService usuarioService;

        @Autowired
        private ReservaService reservaService;

        @Operation(summary = "Obtener todas las reseñas (Admin)", description = "Retorna una lista de todas las reseñas del sistema. Solo accesible por administradores")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de reseñas obtenida exitosamente", content = @Content(schema = @Schema(implementation = Resenia.class))),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol de administrador")
        })
        @GetMapping("/admin")
        @PreAuthorize("hasRole('ADMINISTRADOR')")
        public ResponseEntity<List<Resenia>> getAllResenias() {
                return ResponseEntity.ok(reseniaService.findAll());
        }

        @Operation(summary = "Obtener mis reseñas (Cliente)", description = "Retorna una lista de las reseñas del usuario autenticado")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de reseñas obtenida exitosamente", content = @Content(schema = @Schema(implementation = Resenia.class))),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol de cliente")
        })
        @GetMapping("/cliente/mis-resenias")
        @PreAuthorize("hasRole('CLIENTE')")
        public ResponseEntity<List<Resenia>> getMisResenias() {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                Usuario usuario = usuarioService.findByEmail(auth.getName())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                return ResponseEntity.ok(reseniaService.findByUsuario(usuario));
        }

        @Operation(summary = "Obtener reseña por ID", description = "Retorna una reseña específica basada en su ID. Solo accesible por el propietario de la reseña o un administrador")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Reseña encontrada exitosamente", content = @Content(schema = @Schema(implementation = Resenia.class))),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado. No eres el propietario de la reseña ni un administrador"),
                        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
        })
        @GetMapping("/{id}")
        public ResponseEntity<? extends Object> getReseniaById(
                        @Parameter(description = "ID de la reseña", required = true) @PathVariable Integer id) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                Usuario usuario = usuarioService.findByEmail(auth.getName())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                return reseniaService.findById(id)
                                .map(resenia -> {
                                        if (auth.getAuthorities().stream()
                                                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR")) ||
                                                        resenia.getReserva().getUsuario().getIdUsu()
                                                                        .equals(usuario.getIdUsu())) {
                                                return ResponseEntity.ok(resenia);
                                        }
                                        return ResponseEntity.status(403).build();
                                })
                                .orElse(ResponseEntity.notFound().build());
        }

        @Operation(summary = "Obtener reseñas por usuario", description = "Retorna una lista de reseñas asociadas a un usuario específico")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de reseñas obtenida exitosamente", content = @Content(schema = @Schema(implementation = Resenia.class))),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        })
        @GetMapping("/usuario/{usuarioId}")
        public ResponseEntity<List<Resenia>> getReseniasByUsuario(
                        @Parameter(description = "ID del usuario", required = true) @PathVariable Integer usuarioId) {
                return ResponseEntity.ok(reseniaService.findAll());
        }

        @Operation(summary = "Obtener reseñas por vehículo", description = "Retorna una lista de reseñas asociadas a un vehículo específico")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de reseñas obtenida exitosamente", content = @Content(schema = @Schema(implementation = Resenia.class))),
                        @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
        })
        @GetMapping("/vehiculo/{vehiculoId}")
        public ResponseEntity<List<Resenia>> getReseniasByVehiculo(
                        @Parameter(description = "ID del vehículo", required = true) @PathVariable Integer vehiculoId) {
                return ResponseEntity.ok(reseniaService.findAll());
        }

        @PostMapping
        @PreAuthorize("hasRole('CLIENTE')")
        public ResponseEntity<?> createResenia(@RequestBody Resenia resenia) {
                try {
                        // Obtener el usuario autenticado
                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        Usuario usuario = usuarioService.findByEmail(auth.getName())
                                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                        // Cargar la reserva completa
                        Reserva reserva = reservaService.findById(resenia.getReserva().getIdReser())
                                        .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

                        // Verificar que la reserva pertenece al usuario autenticado
                        if (!reserva.getUsuario().getIdUsu().equals(usuario.getIdUsu())) {
                                Map<String, String> response = new HashMap<>();
                                response.put("error", "No tienes permiso para crear una reseña para esta reserva");
                                return ResponseEntity.status(403).body(response);
                        }

                        // Verificar si ya existe una reseña para esta reserva
                        Optional<Resenia> existingResenia = reseniaService.findByReserva(reserva);
                        if (existingResenia.isPresent()) {
                                // Si existe, actualizar la reseña existente
                                Resenia updatedResenia = existingResenia.get();
                                updatedResenia.setPuntuacion(resenia.getPuntuacion());
                                updatedResenia.setComentario(resenia.getComentario());
                                updatedResenia.setFecha(LocalDate.now());
                                return ResponseEntity.ok(reseniaService.save(updatedResenia));
                        } else {
                                // Si no existe, crear una nueva reseña
                                resenia.setReserva(reserva);
                                resenia.setFecha(LocalDate.now());
                                return ResponseEntity.ok(reseniaService.save(resenia));
                        }
                } catch (Exception e) {
                        Map<String, String> response = new HashMap<>();
                        response.put("error", "Error al procesar la reseña: " + e.getMessage());
                        return ResponseEntity.badRequest().body(response);
                }
        }

        @PutMapping("/{id}")
        public ResponseEntity<? extends Object> updateResenia(@PathVariable Integer id, @RequestBody Resenia resenia) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                Usuario usuario = usuarioService.findByEmail(auth.getName())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                return reseniaService.findById(id)
                                .map(existingResenia -> {
                                        // Verificar si el usuario es admin o el propietario de la reseña
                                        if (auth.getAuthorities().stream()
                                                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR")) ||
                                                        existingResenia.getReserva().getUsuario().getIdUsu()
                                                                        .equals(usuario.getIdUsu())) {
                                                resenia.setIdRese(id);
                                                resenia.setReserva(existingResenia.getReserva()); // Mantener la reserva
                                                                                                  // original
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
                                        if (auth.getAuthorities().stream()
                                                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR")) ||
                                                        resenia.getReserva().getUsuario().getIdUsu()
                                                                        .equals(usuario.getIdUsu())) {
                                                reseniaService.deleteById(id);
                                                return ResponseEntity.ok().<Void>build();
                                        }
                                        return ResponseEntity.status(403).build();
                                })
                                .orElse(ResponseEntity.notFound().build());
        }

        @GetMapping("/exists/{reservaId}")
        @PreAuthorize("hasRole('CLIENTE')")
        public ResponseEntity<Boolean> existsByReserva(@PathVariable Integer reservaId) {
                try {
                        Reserva reserva = reservaService.findById(reservaId)
                                        .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

                        // Verificar que la reserva pertenece al usuario autenticado
                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        Usuario usuario = usuarioService.findByEmail(auth.getName())
                                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                        if (!reserva.getUsuario().getIdUsu().equals(usuario.getIdUsu())) {
                                return ResponseEntity.ok(false);
                        }

                        boolean exists = reseniaService.existsByReserva(reserva);
                        return ResponseEntity.ok(exists);
                } catch (Exception e) {
                        return ResponseEntity.ok(false);
                }
        }

        @GetMapping("/reserva/{reservaId}")
        @PreAuthorize("hasRole('CLIENTE')")
        public ResponseEntity<?> getReseniaByReserva(@PathVariable Integer reservaId) {
                try {
                        // Obtener el usuario autenticado
                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        Usuario usuario = usuarioService.findByEmail(auth.getName())
                                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                        // Buscar la reserva
                        Reserva reserva = reservaService.findById(reservaId)
                                        .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

                        // Verificar que la reserva pertenece al usuario autenticado
                        if (!reserva.getUsuario().getIdUsu().equals(usuario.getIdUsu())) {
                                Map<String, String> response = new HashMap<>();
                                response.put("error", "No tienes permiso para ver esta reseña");
                                return ResponseEntity.status(403).body(response);
                        }

                        // Buscar la reseña
                        Optional<Resenia> resenia = reseniaService.findByReserva(reserva);
                        if (resenia.isPresent()) {
                                return ResponseEntity.ok(resenia.get());
                        } else {
                                Map<String, String> response = new HashMap<>();
                                response.put("error", "No se encontró la reseña");
                                return ResponseEntity.status(404).body(response);
                        }
                } catch (RuntimeException e) {
                        Map<String, String> response = new HashMap<>();
                        response.put("error", e.getMessage());
                        return ResponseEntity.badRequest().body(response);
                } catch (Exception e) {
                        Map<String, String> response = new HashMap<>();
                        response.put("error", "Error al obtener la reseña: " + e.getMessage());
                        return ResponseEntity.status(500).body(response);
                }
        }
}