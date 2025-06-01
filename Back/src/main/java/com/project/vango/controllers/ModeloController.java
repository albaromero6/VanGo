package com.project.vango.controllers;

import com.project.vango.models.Modelo;
import com.project.vango.services.ModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@RestController
@RequestMapping("/api/modelos")
@CrossOrigin(origins = "*")
@Tag(name = "Modelos", description = "API para la gestión de modelos de vehículos")
public class ModeloController {

    @Autowired
    private ModeloService modeloService;

    @Operation(summary = "Obtener todos los modelos", description = "Retorna una lista de todos los modelos de vehículos disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de modelos obtenida exitosamente", content = @Content(schema = @Schema(implementation = Modelo.class)))
    })
    @GetMapping
    public ResponseEntity<List<Modelo>> getAllModelos() {
        return ResponseEntity.ok(modeloService.findAll());
    }

    @Operation(summary = "Obtener modelo por ID", description = "Retorna un modelo específico basado en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modelo encontrado exitosamente", content = @Content(schema = @Schema(implementation = Modelo.class))),
            @ApiResponse(responseCode = "404", description = "Modelo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Modelo> getModeloById(
            @Parameter(description = "ID del modelo", required = true) @PathVariable Integer id) {
        return modeloService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear nuevo modelo", description = "Crea un nuevo modelo de vehículo en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modelo creado exitosamente", content = @Content(schema = @Schema(implementation = Modelo.class))),
            @ApiResponse(responseCode = "400", description = "Datos del modelo inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe un modelo con ese nombre para la marca especificada")
    })
    @PostMapping
    public ResponseEntity<Modelo> createModelo(
            @Parameter(description = "Datos del modelo", required = true) @RequestBody Modelo modelo) {
        return ResponseEntity.ok(modeloService.save(modelo));
    }

    @Operation(summary = "Actualizar modelo", description = "Actualiza los datos de un modelo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modelo actualizado exitosamente", content = @Content(schema = @Schema(implementation = Modelo.class))),
            @ApiResponse(responseCode = "404", description = "Modelo no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos del modelo inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Modelo> updateModelo(
            @Parameter(description = "ID del modelo", required = true) @PathVariable Integer id,
            @Parameter(description = "Datos actualizados del modelo", required = true) @RequestBody Modelo modelo) {
        return modeloService.findById(id)
                .map(existingModelo -> {
                    modelo.setIdMod(id);
                    return ResponseEntity.ok(modeloService.save(modelo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar modelo", description = "Elimina un modelo del sistema. También elimina todos los vehículos asociados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modelo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Modelo no encontrado"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar el modelo porque tiene vehículos asociados")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModelo(
            @Parameter(description = "ID del modelo", required = true) @PathVariable Integer id) {
        return modeloService.findById(id)
                .map(modelo -> {
                    modeloService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}