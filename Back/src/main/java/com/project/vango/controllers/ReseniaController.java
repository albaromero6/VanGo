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
@RequestMapping("/api/resenias")
@CrossOrigin(origins = "*")
@Tag(name = "Reseñas", description = "API para la gestión de reseñas de vehículos")
@SecurityRequirement(name = "bearerAuth")
public class ReseniaController {

    @Autowired
    private ReseniaService reseniaService;

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Obtener todas las reseñas (Admin)", description = "Retorna una lista de todas las reseñas del sistema. Solo accesible por administradores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reseñas obtenida exitosamente", content = @Content(schema = @Schema(implementation = Resenia.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol de administrador")
    })
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Resenia>> getAllResenias() {
        return ResponseEntity.ok(reseniaService.findAll());
    }

    @Operation(summary = "Obtener mis reseñas (Cliente)", description = "Retorna una lista de las reseñas del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de reseñas obtenida exitosamente", content = @Content(schema = @Schema(implementation = Resenia.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol de cliente")
    })
    @GetMapping("/cliente/mis-resenias")
    @PreAuthorize("hasRole('CLIENT')")
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
                    if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ||
                            resenia.getReserva().getUsuario().getIdUsu().equals(usuario.getIdUsu())) {
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
                        resenia.setIdRese(id);
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