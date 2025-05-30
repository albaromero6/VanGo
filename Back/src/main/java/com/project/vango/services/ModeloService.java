package com.project.vango.services;

import com.project.vango.models.Modelo;
import com.project.vango.repositories.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ModeloService {

    @Autowired
    private ModeloRepository modeloRepository;

    public List<Modelo> findAll() {
        return modeloRepository.findAll();
    }

    public Optional<Modelo> findById(Integer id) {
        return modeloRepository.findById(id);
    }

    public Modelo save(Modelo modelo) {
        return modeloRepository.save(modelo);
    }

    public void deleteById(Integer id) {
        modeloRepository.deleteById(id);
    }

    public List<Modelo> findByMarcaId(Integer marcaId) {
        return modeloRepository.findByMarcaIdMar(marcaId);
    }
}