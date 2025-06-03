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
            // Verificar si el usuario existe antes de intentar autenticar
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        logger.error("Usuario no encontrado en la base de datos: {}", email);
                        return new RuntimeException("Usuario no encontrado");
                    });
            logger.debug("Usuario encontrado en la base de datos: {}", usuario.getNombre());
            logger.debug("Rol del usuario: {}", usuario.getRol());

            logger.debug("Iniciando autenticación con AuthenticationManager");
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, password));
                logger.info("Autenticación exitosa para email: {}", email);
            } catch (Exception e) {
                logger.error("Error en la autenticación: {}", e.getMessage());
                throw new RuntimeException("Credenciales inválidas");
            }

            // Generar el token con el email del usuario, su nombre y su rol
            String rol = usuario.getRol().name();
            logger.debug("Generando token con rol: {}", rol);
            String token = jwtUtil.generateToken(email, usuario.getNombre(), usuario.getApellido(), rol);
            logger.debug("Token generado exitosamente para usuario: {} con rol: {}", usuario.getNombre(), rol);
            return token;
        } catch (Exception e) {
            logger.error("Error en el proceso de login para email {}: {}", email, e.getMessage(), e);
            throw e;
        }
    }

    public boolean checkDniExists(String dni) {
        logger.info("Verificando si existe el DNI: {}", dni);
        return usuarioRepository.existsByDni(dni);
    }

    public boolean checkEmailExists(String email) {
        logger.info("Verificando si existe el email: {}", email);
        return usuarioRepository.existsByEmail(email);
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