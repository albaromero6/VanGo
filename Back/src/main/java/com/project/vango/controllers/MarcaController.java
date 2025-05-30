package com.project.vango.controllers;

import com.project.vango.models.Marca;
import com.project.vango.models.Modelo;
import com.project.vango.services.MarcaService;
import com.project.vango.services.ModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/marcas")
@CrossOrigin(origins = "*")
public class MarcaController {

    @Autowired
    private MarcaService marcaService;

    @Autowired
    private ModeloService modeloService;

    @GetMapping
    public ResponseEntity<List<Marca>> getAllMarcas() {
        return ResponseEntity.ok(marcaService.findAll());
    }

    @GetMapping("/{marcaId}/modelos")
    public ResponseEntity<List<Modelo>> getModelosByMarca(@PathVariable Integer marcaId) {
        return ResponseEntity.ok(modeloService.findByMarcaId(marcaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Marca> getMarcaById(@PathVariable Integer id) {
        return marcaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Marca> createMarca(@RequestBody Marca marca) {
        return ResponseEntity.ok(marcaService.save(marca));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Marca> updateMarca(@PathVariable Integer id, @RequestBody Marca marca) {
        return marcaService.findById(id)
                .map(existingMarca -> {
                    marca.setIdMar(id);
                    marca.setModelos(existingMarca.getModelos());
                    return ResponseEntity.ok(marcaService.save(marca));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarca(@PathVariable Integer id) {
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