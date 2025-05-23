package com.project.vango.services;

import com.project.vango.models.Usuario;
import com.project.vango.repositories.UsuarioRepository;
import com.project.vango.config.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public String login(String email, String password) {
        logger.info("Intentando login para email: {}", email);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
            logger.info("Autenticación exitosa para email: {}", email);

            // Obtener el usuario de la base de datos
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Generar el token con el nombre del usuario
            return jwtUtil.generateToken(usuario.getNombre());
        } catch (Exception e) {
            logger.error("Error en autenticación para email {}: {}", email, e.getMessage());
            throw e;
        }
    }

    public Usuario register(Usuario usuario) {
        Assert.notNull(usuario.getPassword(), "La contraseña no puede ser nula");
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        if (usuario.getRegistro() == null) {
            usuario.setRegistro(java.time.LocalDate.now());
        }
        return usuarioRepository.save(usuario);
    }
}