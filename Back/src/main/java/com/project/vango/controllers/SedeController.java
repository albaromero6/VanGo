package com.project.vango.controllers;

import com.project.vango.models.Sede;
import com.project.vango.services.SedeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sedes")
@CrossOrigin(origins = "http://localhost:4200", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE })
public class SedeController {

    @Autowired
    private SedeService sedeService;

    private final Path uploadDir = Paths.get("uploads/sedes");

    @GetMapping
    public ResponseEntity<List<Sede>> getAllSedes() {
        return ResponseEntity.ok(sedeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sede> getSedeById(@PathVariable Integer id) {
        return sedeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Sede> createSede(@RequestBody Sede sede) {
        return ResponseEntity.ok(sedeService.save(sede));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sede> updateSede(@PathVariable Integer id, @RequestBody Sede sede) {
        return sedeService.findById(id)
                .map(existingSede -> {
                    sede.setIdSed(id);
                    return ResponseEntity.ok(sedeService.save(sede));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSede(@PathVariable Integer id) {
        return sedeService.findById(id)
                .map(sede -> {
                    sedeService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/imagen")
    public ResponseEntity<Sede> uploadImagen(
            @PathVariable Integer id,
            @RequestParam("imagen") MultipartFile imagen) {

        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String filename = UUID.randomUUID().toString() + "_" + imagen.getOriginalFilename();
            Path filePath = uploadDir.resolve(filename);

            Files.copy(imagen.getInputStream(), filePath);

            return sedeService.findById(id)
                    .map(sede -> {
                        sede.setImagen(filename);
                        return ResponseEntity.ok(sedeService.save(sede));
                    })
                    .orElse(ResponseEntity.notFound().build());

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}