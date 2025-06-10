package com.project.vango.controllers;

import com.project.vango.models.Usuario;
import com.project.vango.services.UsuarioService;
import com.project.vango.services.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
@Tag(name = "Usuarios", description = "API para la gestión de usuarios")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

        @Autowired
        private UsuarioService usuarioService;

        @Autowired
        private ReservaService reservaService;

        @Operation(summary = "Obtener perfil del usuario autenticado", description = "Retorna los datos del usuario que ha iniciado sesión")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping("/perfil")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<Usuario> getPerfil() {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                System.out.println("Authorities: " + auth.getAuthorities());
                String email = auth.getName();
                return usuarioService.findByEmail(email)
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
        }

        @Operation(summary = "Obtener todos los usuarios", description = "Retorna una lista de todos los usuarios registrados en el sistema")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping
        public ResponseEntity<List<Usuario>> getAllUsuarios() {
                return ResponseEntity.ok(usuarioService.findAll());
        }

        @Operation(summary = "Obtener usuario por ID", description = "Retorna un usuario específico basado en su ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping("/{id}")
        public ResponseEntity<Usuario> getUsuarioById(
                        @Parameter(description = "ID del usuario a buscar", required = true) @PathVariable Integer id) {
                return usuarioService.findById(id)
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
        }

        @Operation(summary = "Obtener usuario por email", description = "Retorna un usuario específico basado en su email")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @GetMapping("/email/{email}")
        public ResponseEntity<Usuario> getUsuarioByEmail(
                        @Parameter(description = "Email del usuario a buscar", required = true) @PathVariable String email) {
                return usuarioService.findByEmail(email)
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
        }

        @Operation(summary = "Crear nuevo usuario", description = "Crea un nuevo usuario en el sistema")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
                        @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @PostMapping
        public ResponseEntity<Usuario> createUsuario(
                        @Parameter(description = "Datos del usuario a crear", required = true) @RequestBody Usuario usuario) {
                return ResponseEntity.ok(usuarioService.save(usuario));
        }

        @Operation(summary = "Actualizar usuario", description = "Actualiza la información de un usuario existente")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
                        @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ADMINISTRADOR')")
        public ResponseEntity<Usuario> updateUsuario(
                        @Parameter(description = "ID del usuario a actualizar", required = true) @PathVariable Integer id,
                        @Parameter(description = "Nuevos datos del usuario", required = true) @RequestBody Usuario usuario,
                        @RequestHeader("Authorization") String token) {
                try {
                        return usuarioService.findById(id)
                                        .map(existingUsuario -> {
                                                // Preserve the ID
                                                usuario.setIdUsu(id);

                                                // If password is not provided in the update, keep the existing one
                                                if (usuario.getPassword() == null
                                                                || usuario.getPassword().trim().isEmpty()) {
                                                        usuario.setPassword(existingUsuario.getPassword());
                                                }

                                                // Validate role change
                                                if (usuario.getRol() != existingUsuario.getRol()) {
                                                        // Check if trying to change the last administrator
                                                        if (existingUsuario.getRol() == Usuario.Rol.ADMINISTRADOR) {
                                                                long adminCount = usuarioService
                                                                                .countByRol(Usuario.Rol.ADMINISTRADOR);
                                                                if (adminCount <= 1) {
                                                                        throw new RuntimeException(
                                                                                        "No se puede cambiar el rol del último administrador");
                                                                }
                                                        }
                                                }

                                                return ResponseEntity.ok(usuarioService.save(usuario));
                                        })
                                        .orElse(ResponseEntity.notFound().build());
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
        }

        @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteUsuario(
                        @Parameter(description = "ID del usuario a eliminar", required = true) @PathVariable Integer id) {
                return usuarioService.findById(id)
                                .map(usuario -> {
                                        usuarioService.deleteById(id);
                                        return ResponseEntity.ok().<Void>build();
                                })
                                .orElse(ResponseEntity.notFound().build());
        }

        @Operation(summary = "Actualizar perfil del usuario autenticado", description = "Actualiza los datos del usuario que ha iniciado sesión")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
                        @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos"),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @PutMapping("/perfil")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<Usuario> updatePerfil(@RequestBody Usuario usuario) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String email = auth.getName();
                return usuarioService.findByEmail(email)
                                .map(existingUsuario -> {
                                        usuario.setIdUsu(existingUsuario.getIdUsu());
                                        usuario.setEmail(existingUsuario.getEmail()); // No permitir cambiar el email
                                        usuario.setPassword(existingUsuario.getPassword()); // No permitir cambiar la
                                                                                            // contraseña
                                        usuario.setRol(existingUsuario.getRol()); // No permitir cambiar el rol
                                        return ResponseEntity.ok(usuarioService.save(usuario));
                                })
                                .orElse(ResponseEntity.notFound().build());
        }

        @Operation(summary = "Eliminar perfil del usuario autenticado", description = "Elimina la cuenta del usuario que ha iniciado sesión")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Perfil eliminado exitosamente"),
                        @ApiResponse(responseCode = "401", description = "No autorizado"),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @DeleteMapping("/perfil")
        @PreAuthorize("isAuthenticated()")
        @Transactional
        public ResponseEntity<?> deletePerfil() {
                try {
                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        String email = auth.getName();
                        return usuarioService.findByEmail(email)
                                        .map(usuario -> {
                                                try {
                                                        // Primero eliminamos todas las reservas del usuario
                                                        reservaService.deleteByUsuarioId(usuario.getIdUsu());
                                                        // Luego eliminamos el usuario
                                                        usuarioService.deleteById(usuario.getIdUsu());
                                                        return ResponseEntity.ok()
                                                                        .body(Map.of("message",
                                                                                        "Usuario eliminado exitosamente"));
                                                } catch (Exception e) {
                                                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                                        .body(Map.of("error",
                                                                                        "Error al eliminar el usuario: "
                                                                                                        + e.getMessage()));
                                                }
                                        })
                                        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                                        .body(Map.of("error", "Usuario no encontrado")));
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
                }
        }
}