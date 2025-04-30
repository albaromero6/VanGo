package com.project.vango.services;

import com.project.vango.models.Marca;
import com.project.vango.repositories.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MarcaService {

    @Autowired
    private MarcaRepository marcaRepository;

    public List<Marca> findAll() 
    {
        return marcaRepository.findAll();
    }

    public Optional<Marca> findById(Integer id) 
    {
        return marcaRepository.findById(id);
    }

    public Marca save(Marca marca) 
    {
        return marcaRepository.save(marca);
    }

    public void deleteById(Integer id) 
    {
        marcaRepository.deleteById(id);
    }
}