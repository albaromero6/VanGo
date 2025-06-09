package com.project.vango.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AdminViewController {

    private static final Logger logger = LoggerFactory.getLogger(AdminViewController.class);

    @GetMapping("/admin")
    public String adminPanel(@RequestParam(required = false) String token,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) throws IOException {
        logger.debug("Accediendo a /admin con token: {}", token != null ? "presente" : "ausente");

        Authentication auth = (Authentication) request.getUserPrincipal();
        logger.debug("Autenticación actual: {}", auth != null ? auth.getName() : "null");

        if (token != null && !token.isEmpty()) {
            // Establecer el token en una cookie
            response.addHeader("Set-Cookie", "token=" + token + "; Path=/; HttpOnly; SameSite=Strict");
            // Redirigir a la misma URL sin el token para evitar que quede en la URL
            response.sendRedirect("/admin");
            return null;
        }

        if (auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"))) {
            model.addAttribute("username", auth.getName());
            return "admin/panel";
        }

        logger.warn("Acceso denegado a /admin");
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
        return null;
    }
}