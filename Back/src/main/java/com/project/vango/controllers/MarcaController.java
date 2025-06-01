package com.project.vango.controllers;

import com.project.vango.models.Marca;
import com.project.vango.models.Modelo;
import com.project.vango.services.MarcaService;
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
@RequestMapping("/api/marcas")
@CrossOrigin(origins = "*")
@Tag(name = "Marcas", description = "API para la gestión de marcas de vehículos")
public class MarcaController {

    @Autowired
    private MarcaService marcaService;

    @Autowired
    private ModeloService modeloService;

    @Operation(summary = "Obtener todas las marcas", description = "Retorna una lista de todas las marcas de vehículos disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de marcas obtenida exitosamente", content = @Content(schema = @Schema(implementation = Marca.class)))
    })
    @GetMapping
    public ResponseEntity<List<Marca>> getAllMarcas() {
        return ResponseEntity.ok(marcaService.findAll());
    }

    @Operation(summary = "Obtener modelos por marca", description = "Retorna una lista de modelos asociados a una marca específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de modelos obtenida exitosamente", content = @Content(schema = @Schema(implementation = Modelo.class))),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada")
    })
    @GetMapping("/{marcaId}/modelos")
    public ResponseEntity<List<Modelo>> getModelosByMarca(
            @Parameter(description = "ID de la marca", required = true) @PathVariable Integer marcaId) {
        return ResponseEntity.ok(modeloService.findByMarcaId(marcaId));
    }

    @Operation(summary = "Obtener marca por ID", description = "Retorna una marca específica basada en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marca encontrada exitosamente", content = @Content(schema = @Schema(implementation = Marca.class))),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Marca> getMarcaById(
            @Parameter(description = "ID de la marca", required = true) @PathVariable Integer id) {
        return marcaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear nueva marca", description = "Crea una nueva marca de vehículo en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marca creada exitosamente", content = @Content(schema = @Schema(implementation = Marca.class))),
            @ApiResponse(responseCode = "400", description = "Datos de marca inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe una marca con ese nombre")
    })
    @PostMapping
    public ResponseEntity<Marca> createMarca(
            @Parameter(description = "Datos de la marca", required = true) @RequestBody Marca marca) {
        return ResponseEntity.ok(marcaService.save(marca));
    }

    @Operation(summary = "Actualizar marca", description = "Actualiza los datos de una marca existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marca actualizada exitosamente", content = @Content(schema = @Schema(implementation = Marca.class))),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de marca inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Marca> updateMarca(
            @Parameter(description = "ID de la marca", required = true) @PathVariable Integer id,
            @Parameter(description = "Datos actualizados de la marca", required = true) @RequestBody Marca marca) {
        return marcaService.findById(id)
                .map(existingMarca -> {
                    marca.setIdMar(id);
                    marca.setModelos(existingMarca.getModelos());
                    return ResponseEntity.ok(marcaService.save(marca));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar marca", description = "Elimina una marca del sistema. También elimina todos los modelos asociados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marca eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar la marca porque tiene modelos asociados")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarca(
            @Parameter(description = "ID de la marca", required = true) @PathVariable Integer id) {
        return marcaService.findById(id)
                .map(marca -> {
                    if (marca.getModelos() != null) {
                        marca.getModelos().forEach(modelo -> {
                            if (modelo.getVehiculos() != null) {
                                modelo.getVehiculos().forEach(vehiculo -> {
                                    vehiculo.setModelo(null);
                                });
                                modelo.getVehiculos().clear();
                            }
                        });
                    }
                    marcaService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}