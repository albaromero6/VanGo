package com.project.vango.controllers;

import com.project.vango.models.Vehiculo;
import com.project.vango.services.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
@RequestMapping("/api/vehiculos")
@CrossOrigin(origins = "*")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    private final Path uploadDir = Paths.get("uploads/vehiculos");

    @GetMapping
    public ResponseEntity<List<Vehiculo>> getAllVehiculos() {
        return ResponseEntity.ok(vehiculoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> getVehiculoById(@PathVariable Integer id) {
        return vehiculoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Vehiculo> createVehiculo(@RequestBody Vehiculo vehiculo) {
        return ResponseEntity.ok(vehiculoService.save(vehiculo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> updateVehiculo(@PathVariable Integer id, @RequestBody Vehiculo vehiculo) {
        return vehiculoService.findById(id)
                .map(existingVehiculo -> {
                    vehiculo.setIdVeh(id);
                    return ResponseEntity.ok(vehiculoService.save(vehiculo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehiculo(@PathVariable Integer id) {
        return vehiculoService.findById(id)
                .map(vehiculo -> {
                    vehiculoService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/imagen/{filename}")
    public ResponseEntity<byte[]> getImagen(@PathVariable String filename) {
        try {
            Path filePath = uploadDir.resolve(filename);
            byte[] imageBytes = Files.readAllBytes(filePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/imagen")
    public ResponseEntity<Vehiculo> uploadImagen(
            @PathVariable Integer id,
            @RequestParam("imagen") MultipartFile imagen) {

        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String filename = UUID.randomUUID().toString() + "_" + imagen.getOriginalFilename();
            Path filePath = uploadDir.resolve(filename);

            Files.copy(imagen.getInputStream(), filePath);

            return vehiculoService.findById(id)
                    .map(vehiculo -> {
                        vehiculo.setImagen(filename);
                        return ResponseEntity.ok(vehiculoService.save(vehiculo));
                    })
                    .orElse(ResponseEntity.notFound().build());

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}