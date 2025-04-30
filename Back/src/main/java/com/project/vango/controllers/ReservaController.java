package com.project.vango.controllers;

import com.project.vango.models.Reserva;
import com.project.vango.services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public ResponseEntity<List<Reserva>> getAllReservas() {
        return ResponseEntity.ok(reservaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getReservaById(@PathVariable Integer id) {
        return reservaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Reserva>> getReservasByUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(reservaService.findAll());
    }

    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<Reserva>> getReservasByVehiculo(@PathVariable Integer vehiculoId) {
        return ResponseEntity.ok(reservaService.findAll());
    }

    @PostMapping
    public ResponseEntity<Reserva> createReserva(@RequestBody Reserva reserva) {
        return ResponseEntity.ok(reservaService.save(reserva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> updateReserva(@PathVariable Integer id, @RequestBody Reserva reserva) {
        return reservaService.findById(id)
                .map(existingReserva -> {
                    reserva.setIdReser(id);
                    return ResponseEntity.ok(reservaService.save(reserva));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable Integer id) {
        return reservaService.findById(id)
                .map(reserva -> {
                    reservaService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Reserva> updateEstado(
            @PathVariable Integer id,
            @RequestParam Reserva.Estado estado) {
        return reservaService.findById(id)
                .map(reserva -> {
                    reserva.setEstado(estado);
                    return ResponseEntity.ok(reservaService.save(reserva));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}