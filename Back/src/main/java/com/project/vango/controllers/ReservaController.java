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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
@Tag(name = "Reservas", description = "API para la gestión de reservas de vehículos")
@SecurityRequirement(name = "bearerAuth")
public class ReservaController {

        @Autowired
        private ReservaService reservaService;

        @Autowired
        private UsuarioService usuarioService;

        @Operation(summary = "Obtener todas las reservas (Admin)", description = "Retorna una lista de todas las reservas del sistema. Solo accesible por administradores")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida exitosamente", content = @Content(schema = @Schema(implementation = Reserva.class))),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol de administrador")
        })
        @GetMapping("/admin")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<List<Reserva>> getAllReservas() {
                return ResponseEntity.ok(reservaService.findAll());
        }

        @Operation(summary = "Obtener mis reservas (Cliente)", description = "Retorna una lista de las reservas del usuario autenticado")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida exitosamente", content = @Content(schema = @Schema(implementation = Reserva.class))),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol de cliente")
        })
        @GetMapping("/cliente/mis-reservas")
        @PreAuthorize("hasRole('CLIENTE')")
        public ResponseEntity<?> getMisReservas() {
                try {
                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        Usuario usuario = usuarioService.findByEmail(auth.getName())
                                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                        List<Reserva> reservas = reservaService.findByUsuario(usuario);
                        System.out.println("Reservas encontradas para usuario " + usuario.getIdUsu() + ": "
                                        + reservas.size());

                        return ResponseEntity.ok(reservas);
                } catch (Exception e) {
                        System.err.println("Error al obtener reservas: " + e.getMessage());
                        e.printStackTrace();
                        return ResponseEntity.status(500).body("Error al obtener las reservas: " + e.getMessage());
                }
        }

        @Operation(summary = "Obtener reserva por ID", description = "Retorna una reserva específica basada en su ID. Solo accesible por el propietario de la reserva o un administrador")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Reserva encontrada exitosamente", content = @Content(schema = @Schema(implementation = Reserva.class))),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado. No eres el propietario de la reserva ni un administrador"),
                        @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
        })
        @GetMapping("/{id}")
        public ResponseEntity<? extends Object> getReservaById(
                        @Parameter(description = "ID de la reserva", required = true) @PathVariable Integer id) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                Usuario usuario = usuarioService.findByEmail(auth.getName())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                return reservaService.findById(id)
                                .map(reserva -> {
                                        if (auth.getAuthorities().stream()
                                                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ||
                                                        reserva.getUsuario().getIdUsu().equals(usuario.getIdUsu())) {
                                                return ResponseEntity.ok(reserva);
                                        }
                                        return ResponseEntity.status(403).build();
                                })
                                .orElse(ResponseEntity.notFound().build());
        }

        @Operation(summary = "Crear nueva reserva", description = "Crea una nueva reserva de vehículo. Solo accesible por clientes")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Reserva creada exitosamente", content = @Content(schema = @Schema(implementation = Reserva.class))),
                        @ApiResponse(responseCode = "400", description = "Datos de la reserva inválidos"),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol de cliente"),
                        @ApiResponse(responseCode = "409", description = "El vehículo no está disponible en las fechas seleccionadas")
        })
        @PostMapping
        @PreAuthorize("hasAnyRole('CLIENTE', 'ADMINISTRADOR')")
        public ResponseEntity<Reserva> createReserva(
                        @Parameter(description = "Datos de la reserva", required = true) @RequestBody Reserva reserva) {
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
                                        // Verificar si el usuario es administrador o el propietario de la reserva
                                        if (auth.getAuthorities().stream()
                                                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ||
                                                        existingReserva.getUsuario().getIdUsu()
                                                                        .equals(usuario.getIdUsu())) {
                                                reserva.setIdReser(id);
                                                reserva.setUsuario(existingReserva.getUsuario());

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
                                        // Verificar si el usuario es administrador o el propietario de la reserva
                                        if (auth.getAuthorities().stream()
                                                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ||
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