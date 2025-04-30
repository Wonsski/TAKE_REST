package com.example.demo.repository;

import com.example.demo.model.klient.Klient;
import org.springframework.data.repository.CrudRepository;

public interface KlientRepository extends CrudRepository<Klient, Integer> {}
