package com.project.vango.repositories;

import com.project.vango.models.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {
    Page<Vehiculo> findAll(Pageable pageable);
}