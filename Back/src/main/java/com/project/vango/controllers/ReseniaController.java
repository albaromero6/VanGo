package com.project.vango.controllers;

import com.project.vango.models.Resenia;
import com.project.vango.services.ReseniaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/resenias")
@CrossOrigin(origins = "*")
public class ReseniaController {

    @Autowired
    private ReseniaService reseniaService;

    @GetMapping
    public ResponseEntity<List<Resenia>> getAllResenias() {
        return ResponseEntity.ok(reseniaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resenia> getReseniaById(@PathVariable Integer id) {
        return reseniaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Resenia>> getReseniasByUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(reseniaService.findAll());
    }

    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<Resenia>> getReseniasByVehiculo(@PathVariable Integer vehiculoId) {
        return ResponseEntity.ok(reseniaService.findAll());
    }

    @PostMapping
    public ResponseEntity<Resenia> createResenia(@RequestBody Resenia resenia) {
        return ResponseEntity.ok(reseniaService.save(resenia));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resenia> updateResenia(@PathVariable Integer id, @RequestBody Resenia resenia) {
        return reseniaService.findById(id)
                .map(existingResenia -> {
                    resenia.setIdResen(id);
                    return ResponseEntity.ok(reseniaService.save(resenia));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResenia(@PathVariable Integer id) {
        return reseniaService.findById(id)
                .map(resenia -> {
                    reseniaService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}