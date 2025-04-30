package com.example.demo.repository;

import com.example.demo.model.Trasa;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TrasaRepository extends CrudRepository<Trasa, Integer> {
    List<Trasa> findByMiejsceWyjazduContainingIgnoreCase(String miejsceWyjazdu);
}
