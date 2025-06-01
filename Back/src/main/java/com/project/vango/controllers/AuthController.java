package com.project.vango.controllers;

import com.project.vango.models.Usuario;
import com.project.vango.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Endpoints para el registro y login de usuarios")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Iniciar sesión", description = "Autentica a un usuario y devuelve un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Parameter(description = "Credenciales de usuario", required = true) @RequestBody LoginRequest request) {
        String token = authService.login(request.email(), request.password());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @Operation(summary = "Registrar nuevo usuario", description = "Crea una nueva cuenta de usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente", content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos"),
            @ApiResponse(responseCode = "409", description = "El email ya está registrado")
    })
    @PostMapping("/register")
    public ResponseEntity<Usuario> register(
            @Parameter(description = "Datos del nuevo usuario", required = true) @RequestBody Usuario usuario) {
        return ResponseEntity.ok(authService.register(usuario));
    }
}

record LoginRequest(
        @Parameter(description = "Email del usuario", example = "usuario@ejemplo.com") String email,
        @Parameter(description = "Contraseña del usuario", example = "password123") String password) {
}

record LoginResponse(
        @Parameter(description = "Token JWT para autenticación") String token) {
}