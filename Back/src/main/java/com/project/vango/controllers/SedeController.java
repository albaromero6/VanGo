package com.project.vango.controllers;

import com.project.vango.models.Sede;
import com.project.vango.services.SedeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Tag(name = "Sedes", description = "API para la gestión de sedes de alquiler")
public class SedeController {

        @Autowired
        private SedeService sedeService;

        private final Path uploadDir = Paths.get("uploads/sedes");

        @Operation(summary = "Obtener todas las sedes", description = "Retorna una lista de todas las sedes de alquiler disponibles")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de sedes obtenida exitosamente", content = @Content(schema = @Schema(implementation = Sede.class)))
        })
        @GetMapping
        public ResponseEntity<List<Sede>> getAllSedes() {
                return ResponseEntity.ok(sedeService.findAll());
        }

        @Operation(summary = "Obtener sede por ID", description = "Retorna una sede específica basada en su ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Sede encontrada exitosamente", content = @Content(schema = @Schema(implementation = Sede.class))),
                        @ApiResponse(responseCode = "404", description = "Sede no encontrada")
        })
        @GetMapping("/{id}")
        public ResponseEntity<Sede> getSedeById(
                        @Parameter(description = "ID de la sede", required = true) @PathVariable Integer id) {
                return sedeService.findById(id)
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
        }

        @Operation(summary = "Crear nueva sede", description = "Crea una nueva sede de alquiler en el sistema")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Sede creada exitosamente", content = @Content(schema = @Schema(implementation = Sede.class))),
                        @ApiResponse(responseCode = "400", description = "Datos de la sede inválidos"),
                        @ApiResponse(responseCode = "409", description = "Ya existe una sede con esa dirección")
        })
        @PostMapping
        public ResponseEntity<Sede> createSede(
                        @Parameter(description = "Datos de la sede", required = true) @RequestBody Sede sede) {
                return ResponseEntity.ok(sedeService.save(sede));
        }

        @Operation(summary = "Actualizar sede", description = "Actualiza los datos de una sede existente")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Sede actualizada exitosamente", content = @Content(schema = @Schema(implementation = Sede.class))),
                        @ApiResponse(responseCode = "404", description = "Sede no encontrada"),
                        @ApiResponse(responseCode = "400", description = "Datos de la sede inválidos")
        })
        @PutMapping("/{id}")
        public ResponseEntity<Sede> updateSede(
                        @Parameter(description = "ID de la sede", required = true) @PathVariable Integer id,
                        @Parameter(description = "Datos actualizados de la sede", required = true) @RequestBody Sede sede) {
                return sedeService.findById(id)
                                .map(existingSede -> {
                                        sede.setIdSed(id);
                                        return ResponseEntity.ok(sedeService.save(sede));
                                })
                                .orElse(ResponseEntity.notFound().build());
        }

        @Operation(summary = "Eliminar sede", description = "Elimina una sede del sistema")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Sede eliminada exitosamente"),
                        @ApiResponse(responseCode = "404", description = "Sede no encontrada"),
                        @ApiResponse(responseCode = "409", description = "No se puede eliminar la sede porque tiene reservas asociadas")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteSede(
                        @Parameter(description = "ID de la sede", required = true) @PathVariable Integer id) {
                return sedeService.findById(id)
                                .map(sede -> {
                                        sedeService.deleteById(id);
                                        return ResponseEntity.ok().<Void>build();
                                })
                                .orElse(ResponseEntity.notFound().build());
        }

        @Operation(summary = "Subir imagen de sede", description = "Sube una imagen para una sede específica")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Imagen subida exitosamente", content = @Content(schema = @Schema(implementation = Sede.class))),
                        @ApiResponse(responseCode = "404", description = "Sede no encontrada"),
                        @ApiResponse(responseCode = "500", description = "Error al procesar la imagen")
        })
        @PostMapping("/{id}/imagen")
        public ResponseEntity<Sede> uploadImagen(
                        @Parameter(description = "ID de la sede", required = true) @PathVariable Integer id,
                        @Parameter(description = "Archivo de imagen", required = true) @RequestParam("imagen") MultipartFile imagen) {
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

        @GetMapping("/imagen/{filename}")
        @ResponseBody
        public ResponseEntity<byte[]> getImagen(@PathVariable String filename) {
                try {
                        Path filePath = uploadDir.resolve(filename);
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
}