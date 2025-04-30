package com.project.vango.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.vango.models.Vehiculo;
import com.project.vango.repositories.VehiculoRepository;
import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    public List<Vehiculo> findAll() 
    {
        return vehiculoRepository.findAll();
    }

    public Optional<Vehiculo> findById(Integer id) 
    {
        return vehiculoRepository.findById(id);
    }

    public Vehiculo save(Vehiculo vehiculo) 
    {
        return vehiculoRepository.save(vehiculo);
    }

    public void deleteById(Integer id) 
    {
        vehiculoRepository.deleteById(id);
    }
}