package com.project.vango.controllers;

import com.project.vango.models.Sede;
import com.project.vango.services.SedeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sedes")
@CrossOrigin(origins = "*")
public class SedeController {

    @Autowired
    private SedeService sedeService;

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
}