package com.project.vango.services;

import com.project.vango.models.Sede;
import com.project.vango.repositories.SedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SedeService {

    @Autowired
    private SedeRepository sedeRepository;

    public List<Sede> findAll() {
        return sedeRepository.findAll();
    }

    public Optional<Sede> findById(Integer id) {
        return sedeRepository.findById(id);
    }

    public Sede save(Sede sede) {
        return sedeRepository.save(sede);
    }

    public void deleteById(Integer id) {
        sedeRepository.deleteById(id);
    }
}