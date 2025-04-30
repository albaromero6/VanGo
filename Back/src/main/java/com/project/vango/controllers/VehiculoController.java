package com.project.vango.controllers;

import com.project.vango.models.Vehiculo;
import com.project.vango.services.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@CrossOrigin(origins = "*")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

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

    @GetMapping("/disponibles")
    public ResponseEntity<List<Vehiculo>> getVehiculosDisponibles() {
        return ResponseEntity.ok(vehiculoService.findAll());
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

    @PutMapping("/{id}/disponibilidad")
    public ResponseEntity<Vehiculo> updateDisponibilidad(
            @PathVariable Integer id,
            @RequestParam Vehiculo.Disponibilidad disponibilidad) {
        return vehiculoService.findById(id)
                .map(vehiculo -> {
                    vehiculo.setDisponibilidad(disponibilidad);
                    return ResponseEntity.ok(vehiculoService.save(vehiculo));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}