package com.project.vango.services;

import com.project.vango.models.Usuario;
import com.project.vango.repositories.UsuarioRepository;
import com.project.vango.config.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class AuthService {

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
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        return jwtUtil.generateToken(email);
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