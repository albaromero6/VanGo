package com.project.vango.services;

import com.project.vango.models.Vehiculo;
import com.project.vango.repositories.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    public Page<Vehiculo> findAll(Pageable pageable) {
        return vehiculoRepository.findAll(pageable);
    }

    public List<Vehiculo> findAll() {
        return vehiculoRepository.findAll();
    }

    public Optional<Vehiculo> findById(Integer id) {
        return vehiculoRepository.findById(id);
    }

    public Vehiculo save(Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }

    public void deleteById(Integer id) {
        vehiculoRepository.deleteById(id);
    }
}