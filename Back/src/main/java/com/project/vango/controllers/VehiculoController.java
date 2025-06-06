package com.project.vango.controllers;

import com.project.vango.models.Vehiculo;
import com.project.vango.services.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/vehiculos")
@CrossOrigin(origins = "*")
@Tag(name = "Vehículos", description = "API para la gestión de vehículos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    private final Path uploadDir = Paths.get("uploads/vehiculos");

    public VehiculoController() {
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Operation(summary = "Obtener todos los vehículos", description = "Retorna una lista paginada de todos los vehículos disponibles en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vehículos obtenida exitosamente", content = @Content(schema = @Schema(implementation = Vehiculo.class)))
    })
    @GetMapping
    public ResponseEntity<Page<Vehiculo>> getAllVehiculos(
            @Parameter(description = "Número de página (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de la página") @RequestParam(defaultValue = "9") int size) {
        return ResponseEntity.ok(vehiculoService.findAll(PageRequest.of(page, size)));
    }

    @Operation(summary = "Obtener vehículo por ID", description = "Retorna un vehículo específico basado en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo encontrado exitosamente", content = @Content(schema = @Schema(implementation = Vehiculo.class))),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> getVehiculoById(
            @Parameter(description = "ID del vehículo", required = true) @PathVariable Integer id) {
        return vehiculoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear nuevo vehículo", description = "Crea un nuevo vehículo en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo creado exitosamente", content = @Content(schema = @Schema(implementation = Vehiculo.class))),
            @ApiResponse(responseCode = "400", description = "Datos del vehículo inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe un vehículo con esa matrícula")
    })
    @PostMapping
    public ResponseEntity<Vehiculo> createVehiculo(
            @Parameter(description = "Datos del vehículo", required = true) @RequestBody Vehiculo vehiculo) {
        return ResponseEntity.ok(vehiculoService.save(vehiculo));
    }

    @Operation(summary = "Actualizar vehículo", description = "Actualiza los datos de un vehículo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo actualizado exitosamente", content = @Content(schema = @Schema(implementation = Vehiculo.class))),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos del vehículo inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> updateVehiculo(
            @Parameter(description = "ID del vehículo", required = true) @PathVariable Integer id,
            @Parameter(description = "Datos actualizados del vehículo", required = true) @RequestBody Vehiculo vehiculo) {
        return vehiculoService.findById(id)
                .map(existingVehiculo -> {
                    vehiculo.setIdVeh(id);
                    return ResponseEntity.ok(vehiculoService.save(vehiculo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar vehículo", description = "Elimina un vehículo del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar el vehículo porque tiene reservas asociadas")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehiculo(
            @Parameter(description = "ID del vehículo", required = true) @PathVariable Integer id) {
        return vehiculoService.findById(id)
                .map(vehiculo -> {
                    vehiculoService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener imagen del vehículo", description = "Retorna la imagen de un vehículo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagen obtenida exitosamente", content = @Content(mediaType = "image/jpeg")),
            @ApiResponse(responseCode = "404", description = "Imagen no encontrada")
    })
    @GetMapping("/imagen/{filename}")
    public ResponseEntity<byte[]> getImagen(
            @Parameter(description = "Nombre del archivo de imagen", required = true) @PathVariable String filename) {
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
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Subir imagen de vehículo", description = "Sube una imagen para un vehículo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagen subida exitosamente", content = @Content(schema = @Schema(implementation = Vehiculo.class))),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error al procesar la imagen")
    })
    @PostMapping("/{id}/imagen")
    public ResponseEntity<Vehiculo> uploadImagen(
            @Parameter(description = "ID del vehículo", required = true) @PathVariable Integer id,
            @Parameter(description = "Archivo de imagen", required = true) @RequestParam("imagen") MultipartFile imagen) {
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

    @Operation(summary = "Subir imagen de detalle", description = "Sube una imagen de detalle para un vehículo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagen subida exitosamente", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "Archivo vacío o inválido"),
            @ApiResponse(responseCode = "500", description = "Error al procesar la imagen")
    })
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadDetalleImagen(
            @Parameter(description = "Archivo de imagen", required = true) @RequestParam("file") MultipartFile file) {
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadDir.resolve(filename);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }

            Files.copy(file.getInputStream(), filePath);

            Map<String, String> response = new HashMap<>();
            response.put("nombreArchivo", filename);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}