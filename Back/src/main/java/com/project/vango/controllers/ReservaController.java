package com.project.vango.controllers;

import com.project.vango.models.*;
import com.project.vango.services.*;
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
import java.util.Map;
import org.springframework.http.HttpStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

        @Autowired
        private VehiculoService vehiculoService;

        @Autowired
        private SedeService sedeService;

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
        public ResponseEntity<?> createReserva(@RequestBody Map<String, Object> reservaData,
                        Authentication authentication) {
                try {
                        String email = authentication.getName();
                        usuarioService.findByEmail(email)
                                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                        // Obtener el ID del usuario de la reserva
                        Integer idUsuario = Integer.valueOf(reservaData.get("idUsu").toString());
                        Usuario usuarioReserva = usuarioService.findById(idUsuario)
                                        .orElseThrow(() -> new RuntimeException("Usuario de la reserva no encontrado"));

                        // Obtener el vehículo
                        Integer idVehiculo = Integer.valueOf(reservaData.get("idVeh").toString());
                        Vehiculo vehiculo = vehiculoService.findById(idVehiculo)
                                        .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

                        // Obtener las sedes
                        Integer idSedeSalida = Integer.valueOf(reservaData.get("idSedeSalid").toString());
                        Integer idSedeLlegada = Integer.valueOf(reservaData.get("idSedeLleg").toString());

                        Sede sedeSalida = sedeService.findById(idSedeSalida)
                                        .orElseThrow(() -> new RuntimeException("Sede de salida no encontrada"));
                        Sede sedeLlegada = sedeService.findById(idSedeLlegada)
                                        .orElseThrow(() -> new RuntimeException("Sede de llegada no encontrada"));

                        // Crear la reserva
                        Reserva reserva = new Reserva();
                        reserva.setUsuario(usuarioReserva);
                        reserva.setVehiculo(vehiculo);
                        reserva.setIdSed_Salid(sedeSalida);
                        reserva.setIdSed_Lleg(sedeLlegada);

                        // Convertir las fechas de String a LocalDate
                        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                        LocalDateTime inicioDateTime = LocalDateTime.parse(reservaData.get("inicio").toString(),
                                        formatter);
                        LocalDateTime finDateTime = LocalDateTime.parse(reservaData.get("fin").toString(), formatter);
                        reserva.setInicio(inicioDateTime.toLocalDate());
                        reserva.setFin(finDateTime.toLocalDate());

                        reserva.setTotal(Double.valueOf(reservaData.get("total").toString()));

                        // Determinar el estado basado en las fechas
                        LocalDate now = LocalDate.now();
                        if (reserva.getInicio().isBefore(now)) {
                                reserva.setEstado(Reserva.Estado.FINALIZADA);
                        } else {
                                reserva.setEstado(Reserva.Estado.RESERVADA);
                        }

                        Reserva savedReserva = reservaService.save(reserva);
                        return ResponseEntity.ok(savedReserva);
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                        .body(Map.of("error", "Error al crear la reserva: " + e.getMessage()));
                }
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
                try {
                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        Usuario usuario = usuarioService.findByEmail(auth.getName())
                                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                        return reservaService.findById(id)
                                        .map(reserva -> {
                                                // Verificar si el usuario es administrador o el propietario de la
                                                // reserva
                                                if (auth.getAuthorities().stream()
                                                                .anyMatch(a -> a.getAuthority()
                                                                                .equals("ROLE_ADMINISTRADOR"))
                                                                ||
                                                                reserva.getUsuario().getIdUsu()
                                                                                .equals(usuario.getIdUsu())) {
                                                        try {
                                                                reservaService.deleteById(id);
                                                                return ResponseEntity.ok().build();
                                                        } catch (Exception e) {
                                                                return ResponseEntity.status(
                                                                                HttpStatus.INTERNAL_SERVER_ERROR)
                                                                                .body(Map.of("error",
                                                                                                "Error al eliminar la reserva: "
                                                                                                                + e.getMessage()));
                                                        }
                                                }
                                                return ResponseEntity.status(403)
                                                                .body(Map.of("error",
                                                                                "No tienes permisos para eliminar esta reserva"));
                                        })
                                        .orElse(ResponseEntity.notFound().build());
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
                }
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