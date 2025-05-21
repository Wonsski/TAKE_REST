package com.example.demo.repository;

import com.example.demo.model.Przewoz;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PrzewozRepository extends CrudRepository<Przewoz, Integer>, JpaSpecificationExecutor<Przewoz> {
    List<Przewoz> findByCenaBetween(Double cenaMin, Double cenaMax);
}
