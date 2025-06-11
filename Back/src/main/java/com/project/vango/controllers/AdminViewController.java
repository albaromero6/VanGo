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
import com.project.vango.services.MarcaService;
import com.project.vango.services.ModeloService;
import com.project.vango.models.Sede;
import com.project.vango.services.SedeService;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    private static final Logger logger = LoggerFactory.getLogger(AdminViewController.class);
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private MarcaService marcaService;
    @Autowired
    private ModeloService modeloService;
    @Autowired
    private SedeService sedeService;

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
        List<com.project.vango.models.Marca> marcas = marcaService.findAll();

        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("token", token);
        model.addAttribute("marcas", marcas);
        return "admin/marcas";
    }

    @GetMapping("/modelos")
    public String modelos(@RequestParam(required = false) String token, Model model, HttpServletRequest request) {
        if (token == null || token.isEmpty()) {
            return "redirect:/login";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        List<com.project.vango.models.Modelo> modelos = modeloService.findAll();
        List<com.project.vango.models.Marca> marcas = marcaService.findAll();

        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("token", token);
        model.addAttribute("modelos", modelos);
        model.addAttribute("marcas", marcas);
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
        if (token == null || token.isEmpty()) {
            return "redirect:/login";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        List<Sede> sedes = sedeService.findAll();

        model.addAttribute("nombreCompleto", getNombreCompleto(email));
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("token", token);
        model.addAttribute("sedes", sedes);
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

    @PostMapping("/sedes/crear")
    public String crearSede(@RequestParam("direccion") String direccion,
            @RequestParam("ciudad") String ciudad,
            @RequestParam("telefono") String telefono,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            @RequestParam(required = false) String token,
            RedirectAttributes redirectAttributes) {
        try {
            Sede sede = new Sede();
            sede.setDireccion(direccion);
            sede.setCiudad(ciudad);
            sede.setTelefono(telefono);

            if (imagen != null && !imagen.isEmpty()) {
                // Crear el directorio si no existe
                Path uploadDir = Paths.get("uploads/sedes");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                // Validar el tipo de archivo
                String contentType = imagen.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    throw new IllegalArgumentException("El archivo debe ser una imagen");
                }

                // Generar nombre único para el archivo
                String originalFilename = imagen.getOriginalFilename();
                if (originalFilename == null) {
                    throw new IllegalArgumentException("El nombre del archivo no puede ser nulo");
                }

                String filename = UUID.randomUUID().toString() + "_" + originalFilename;
                Path filePath = uploadDir.resolve(filename);

                // Guardar el archivo
                Files.copy(imagen.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                // Guardar el nombre del archivo en la sede
                sede.setImagen(filename);
            }

            sedeService.save(sede);
            redirectAttributes.addFlashAttribute("success", "Sede creada exitosamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear la sede: " + e.getMessage());
        }
        return "redirect:/admin/sedes?token=" + token;
    }

    @DeleteMapping("/sedes/eliminar/{id}")
    public ResponseEntity<Void> eliminarSede(@PathVariable Integer id) {
        try {
            sedeService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/sedes/imagen/{filename}")
    @ResponseBody
    public ResponseEntity<byte[]> getImagen(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads/sedes").resolve(filename);
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = Files.readAllBytes(filePath);
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "image/jpeg";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/sedes/editar/{id}")
    public String editarSede(@PathVariable Integer id,
            @RequestParam("direccion") String direccion,
            @RequestParam("ciudad") String ciudad,
            @RequestParam("telefono") String telefono,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            @RequestParam(required = false) String token,
            RedirectAttributes redirectAttributes) {
        try {
            Sede sede = sedeService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Sede no encontrada"));

            sede.setDireccion(direccion);
            sede.setCiudad(ciudad);
            sede.setTelefono(telefono);

            if (imagen != null && !imagen.isEmpty()) {
                // Crear el directorio si no existe
                Path uploadDir = Paths.get("uploads/sedes");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                // Validar el tipo de archivo
                String contentType = imagen.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    throw new IllegalArgumentException("El archivo debe ser una imagen");
                }

                // Generar nombre único para el archivo
                String originalFilename = imagen.getOriginalFilename();
                if (originalFilename == null) {
                    throw new IllegalArgumentException("El nombre del archivo no puede ser nulo");
                }

                String filename = UUID.randomUUID().toString() + "_" + originalFilename;
                Path filePath = uploadDir.resolve(filename);

                // Guardar el archivo
                Files.copy(imagen.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                // Eliminar la imagen anterior si existe
                if (sede.getImagen() != null) {
                    Path oldFilePath = uploadDir.resolve(sede.getImagen());
                    Files.deleteIfExists(oldFilePath);
                }

                // Guardar el nombre del archivo en la sede
                sede.setImagen(filename);
            }

            sedeService.save(sede);
            redirectAttributes.addFlashAttribute("success", "Sede actualizada exitosamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la sede: " + e.getMessage());
        }
        return "redirect:/admin/sedes?token=" + token;
    }
}