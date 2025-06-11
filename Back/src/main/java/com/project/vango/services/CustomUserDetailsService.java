package com.project.vango.services;

import com.project.vango.models.Usuario;
import com.project.vango.repositories.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        String authority = "ROLE_" + usuario.getRol().name();
        System.out.println("Cargando usuario: " + email);
        System.out.println("Rol del usuario: " + usuario.getRol().name());
        System.out.println("Authority generada: " + authority);

        UserDetails userDetails = new User(
                usuario.getEmail(),
                usuario.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(authority)));

        System.out.println("UserDetails creado: " + userDetails);
        System.out.println("Authorities del UserDetails: " + userDetails.getAuthorities());

        return userDetails;
    }
}