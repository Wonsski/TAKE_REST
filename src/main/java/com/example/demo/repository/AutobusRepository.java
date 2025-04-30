package com.example.demo.repository;

import com.example.demo.model.Autobus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AutobusRepository extends CrudRepository<Autobus, Integer> {
    List<Autobus> findByMarkaContainingIgnoreCase(String marka);
}
