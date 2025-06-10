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
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.dao.DataIntegrityViolationException;

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
    public String adminPanel(@RequestParam(required = false) String token, Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        if (token == null || token.isEmpty()) {
            return "redirect:/login";
        }
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("token", token);
        return "admin/panel";
    }

    @GetMapping("/usuarios")
    public String usuarios(@RequestParam(required = false) String token, Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        List<Usuario> usuarios = usuarioService.findAll();

        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("token", token);
        return "admin/usuarios";
    }

    @GetMapping("/marcas")
    public String marcas(@RequestParam(required = false) String token, Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("token", token);
        return "admin/marcas";
    }

    @GetMapping("/modelos")
    public String modelos(@RequestParam(required = false) String token, Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("token", token);
        return "admin/modelos";
    }

    @GetMapping("/vehiculos")
    public String vehiculos(@RequestParam(required = false) String token, Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("token", token);
        return "admin/vehiculos";
    }

    @GetMapping("/sedes")
    public String sedes(@RequestParam(required = false) String token, Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("token", token);
        return "admin/sedes";
    }

    @GetMapping("/rutas")
    public String rutas(@RequestParam(required = false) String token, Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("token", token);
        return "admin/rutas";
    }

    @GetMapping("/reservas")
    public String reservas(@RequestParam(required = false) String token, Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("token", token);
        return "admin/reservas";
    }

    @GetMapping("/resenas")
    public String resenas(@RequestParam(required = false) String token, Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("token", token);
        return "admin/resenas";
    }

    @GetMapping("/conductores")
    public String conductores(@RequestParam(required = false) String token, Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("token", token);
        return "admin/conductores";
    }

    @GetMapping("/incidencias")
    public String incidencias(@RequestParam(required = false) String token, Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("token", token);
        return "admin/incidencias";
    }

    @GetMapping("/estadisticas")
    public String estadisticas(@RequestParam(required = false) String token, Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("token", token);
        return "admin/estadisticas";
    }

    @GetMapping("/configuracion")
    public String configuracion(@RequestParam(required = false) String token, Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("token", token);
        return "admin/configuracion";
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

    @DeleteMapping("/usuarios/eliminar/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        try {
            usuarioService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/usuarios/eliminar-con-reservas/{id}")
    public ResponseEntity<Void> eliminarUsuarioConReservas(@PathVariable Integer id) {
        try {
            usuarioService.deleteUsuarioConReservas(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}