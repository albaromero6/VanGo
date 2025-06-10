package com.project.vango.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.project.vango.services.UsuarioService;
import com.project.vango.models.Usuario;
import java.util.List;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    private static final Logger logger = LoggerFactory.getLogger(AdminViewController.class);
    @Autowired
    private UsuarioService usuarioService;

    private String getNombreCompleto(String email) {
        return usuarioService.findByEmail(email)
                .map(usuario -> usuario.getNombre() + " " + usuario.getApellido())
                .orElse("Usuario");
    }

    @GetMapping("")
    public String adminRoot() {
        return "redirect:/admin/panel";
    }

    @GetMapping("/panel")
    public String adminPanel(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        return "admin/panel";
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        List<Usuario> usuarios = usuarioService.findAll();

        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("usuarios", usuarios);
        return "admin/usuarios";
    }

    @GetMapping("/marcas")
    public String marcas(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        return "admin/marcas";
    }

    @GetMapping("/modelos")
    public String modelos(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        return "admin/modelos";
    }

    @GetMapping("/vehiculos")
    public String vehiculos(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        return "admin/vehiculos";
    }

    @GetMapping("/sedes")
    public String sedes(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        return "admin/sedes";
    }

    @GetMapping("/rutas")
    public String rutas(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        return "admin/rutas";
    }

    @GetMapping("/reservas")
    public String reservas(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        return "admin/reservas";
    }

    @GetMapping("/resenas")
    public String resenas(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        return "admin/resenas";
    }

    @GetMapping("/conductores")
    public String conductores(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        return "admin/conductores";
    }

    @GetMapping("/incidencias")
    public String incidencias(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        return "admin/incidencias";
    }

    @GetMapping("/estadisticas")
    public String estadisticas(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        return "admin/estadisticas";
    }

    @GetMapping("/configuracion")
    public String configuracion(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        return "admin/configuracion";
    }

    @GetMapping("/admin")
    public String adminPanel(@RequestParam(required = false) String token,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) throws IOException {
        logger.debug("Accediendo a /admin con token: {}", token != null ? "presente" : "ausente");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("Autenticación actual: {}", auth != null ? auth.getName() : "null");

        if (token != null && !token.isEmpty()) {
            response.addHeader("Set-Cookie", "token=" + token + "; Path=/; HttpOnly; SameSite=Strict");
            response.sendRedirect("/admin");
            return null;
        }

        if (auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"))) {

            Usuario usuario = usuarioService.findByEmail(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            String nombreCompleto = usuario.getNombre() + " " + usuario.getApellido();
            model.addAttribute("nombreCompleto", nombreCompleto);
            return "admin/panel";
        }

        logger.warn("Acceso denegado a /admin");
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
        return null;
    }

    @PostMapping("/usuarios/crear")
    public String crearUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.save(usuario);
            redirectAttributes.addFlashAttribute("success", "Usuario creado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el usuario: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/redirect-to-frontend")
    public RedirectView redirectToFrontend(HttpServletRequest request) {
        RedirectView redirectView = new RedirectView();
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        redirectView.setUrl("http://localhost:4200/profile?token=" + token);
        redirectView.setExposeModelAttributes(false);
        return redirectView;
    }
}