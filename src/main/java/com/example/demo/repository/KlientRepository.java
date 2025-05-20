package com.example.demo.repository;

import com.example.demo.model.Klient;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KlientRepository extends CrudRepository<Klient, Integer>, JpaSpecificationExecutor<Klient> {
    List<Klient> findByEmailContainingIgnoreCase(String email);
}
